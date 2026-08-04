package com.example.util

import android.content.ContentValues
import android.content.Context
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.print.PrintAttributes
import android.print.PrintManager
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.data.local.entity.NoteEntity
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfExportHelper {

    fun savePdfToPublicDownloads(context: Context, sourceFile: File, displayName: String): Uri? {
        val resolver = context.contentResolver
        return try {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                }
            }

            val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Downloads.EXTERNAL_CONTENT_URI
            } else {
                MediaStore.Files.getContentUri("external")
            }

            val itemUri = resolver.insert(collection, contentValues)
            if (itemUri != null) {
                resolver.openOutputStream(itemUri)?.use { out ->
                    sourceFile.inputStream().use { input ->
                        input.copyTo(out)
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.clear()
                    contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    resolver.update(itemUri, contentValues, null, null)
                }
                itemUri
            } else {
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!downloadsDir.exists()) downloadsDir.mkdirs()
                val targetFile = File(downloadsDir, displayName)
                sourceFile.copyTo(targetFile, overwrite = true)
                Uri.fromFile(targetFile)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            try {
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!downloadsDir.exists()) downloadsDir.mkdirs()
                val targetFile = File(downloadsDir, displayName)
                sourceFile.copyTo(targetFile, overwrite = true)
                Uri.fromFile(targetFile)
            } catch (ex: Exception) {
                ex.printStackTrace()
                null
            }
        }
    }

    fun buildHtmlForNotes(notes: List<NoteEntity>, isSingleNote: Boolean): String {
        val exportDate = SimpleDateFormat("dd MMMM, yyyy • hh:mm a", Locale("bn", "BD")).format(Date())
        
        val notesHtml = notes.mapIndexed { index, note ->
            val title = if (note.title.isNotBlank()) note.title else "শিরোনামহীন কবিতা"
            val noteDate = SimpleDateFormat("dd MMMM, yyyy • hh:mm a", Locale("bn", "BD")).format(Date(note.createdAt))
            val updatedDate = SimpleDateFormat("dd MMMM, yyyy • hh:mm a", Locale("bn", "BD")).format(Date(note.updatedAt))
            
            val titleColor = if (note.titleColorHex.isNotBlank()) note.titleColorHex else "#B8860B"
            val textColor = if (note.textColorHex.isNotBlank()) note.textColorHex else "#2C2C3A"
            val alignment = note.textAlign.lowercase()
            
            val textDecorations = mutableListOf<String>()
            if (note.isUnderline) textDecorations.add("underline")
            if (note.isStrikethrough) textDecorations.add("line-through")
            val textDecorationCss = if (textDecorations.isNotEmpty()) textDecorations.joinToString(" ") else "none"
            
            val fontWeight = if (note.isBold) "bold" else "normal"
            val fontStyle = if (note.isItalic) "italic" else "normal"
            val fontSizePx = (note.fontSizeSp * 1.25f).coerceAtLeast(15f)
            val lineHeightRatio = note.lineSpacingMultiplier.coerceAtLeast(1.6f)

            val safeTitle = escapeHtml(title)
            val safeContent = escapeHtml(note.content)

            """
            <div class="note-page ${if (index > 0) "page-break" else ""}">
                <div class="header">
                    <div class="app-brand">কাব্যলোকের ব্রক্ষকবি</div>
                    <div class="export-date">তারিখ: $exportDate</div>
                </div>
                <div class="header-line"></div>
                
                <div class="title-container" style="text-align: $alignment;">
                    <h1 class="note-title" style="color: $titleColor;">$safeTitle</h1>
                    <div class="note-meta">
                        <span>রচনাকাল: $noteDate</span>
                        ${if (note.updatedAt > note.createdAt + 60000) " • <span>সম্পাদিত: $updatedDate</span>" else ""}
                    </div>
                </div>

                <div class="divider">
                    <span class="divider-ornament">✦ ❦ ✦</span>
                </div>

                <div class="note-body" style="
                    color: $textColor;
                    font-size: ${fontSizePx}px;
                    font-weight: $fontWeight;
                    font-style: $fontStyle;
                    text-decoration: $textDecorationCss;
                    text-align: $alignment;
                    line-height: $lineHeightRatio;
                ">$safeContent</div>

                <div class="footer">
                    <div class="footer-line"></div>
                    <div class="footer-text">
                        <span>কাব্যলোকের ব্রক্ষকবি — কাব্য সংকলন</span>
                        ${if (!isSingleNote) "<span class='note-count'>নোট ${index + 1} / ${notes.size}</span>" else ""}
                    </div>
                </div>
            </div>
            """.trimIndent()
        }.joinToString("\n")

        return """
        <!DOCTYPE html>
        <html lang="bn">
        <head>
            <meta charset="utf-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                @page {
                    size: A4;
                    margin: 0;
                }
                * {
                    box-sizing: border-box;
                    -webkit-print-color-adjust: exact !important;
                    print-color-adjust: exact !important;
                }
                body {
                    margin: 0;
                    padding: 0;
                    background-color: #FDFBF7;
                    font-family: 'Tiro Bangla', 'Hind Siliguri', 'SolaimanLipi', 'Kalpurush', serif, sans-serif;
                    color: #2B2B36;
                    -webkit-font-smoothing: antialiased;
                }
                .page-break {
                    page-break-before: always !important;
                    break-before: page !important;
                }
                .note-page {
                    padding: 45pt 50pt 45pt 50pt;
                    min-height: 100vh;
                    position: relative;
                    background-color: #FDFBF7;
                }
                .header {
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    padding-bottom: 6pt;
                }
                .app-brand {
                    font-size: 14pt;
                    font-weight: bold;
                    color: #B8860B;
                    letter-spacing: 0.5px;
                }
                .export-date {
                    font-size: 9pt;
                    color: #7A7A8C;
                }
                .header-line {
                    height: 1.5px;
                    background: linear-gradient(to right, #D4A017, #E6CA65, #D4A017);
                    margin-bottom: 24pt;
                }
                .title-container {
                    margin-bottom: 12pt;
                }
                .note-title {
                    font-size: 22pt;
                    margin: 0 0 6pt 0;
                    font-weight: bold;
                    line-height: 1.3;
                }
                .note-meta {
                    font-size: 9.5pt;
                    color: #888899;
                    font-style: italic;
                }
                .divider {
                    text-align: center;
                    margin: 18pt 0 22pt 0;
                }
                .divider-ornament {
                    color: #D4A017;
                    font-size: 11pt;
                    opacity: 0.8;
                }
                .note-body {
                    white-space: pre-wrap;
                    word-wrap: break-word;
                    padding-bottom: 50pt;
                }
                .footer {
                    position: absolute;
                    bottom: 30pt;
                    left: 50pt;
                    right: 50pt;
                }
                .footer-line {
                    height: 1px;
                    background-color: #E2DDD0;
                    margin-bottom: 8pt;
                }
                .footer-text {
                    display: flex;
                    justify-content: space-between;
                    font-size: 8.5pt;
                    color: #8C8C9E;
                }
            </style>
        </head>
        <body>
            $notesHtml
        </body>
        </html>
        """.trimIndent()
    }

    private fun escapeHtml(text: String): String {
        return text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;")
    }

    fun exportToPdf(
        context: Context,
        htmlContent: String,
        outputFile: File,
        onSuccess: (File) -> Unit,
        onError: (Exception) -> Unit
    ) {
        Handler(Looper.getMainLooper()).post {
            try {
                val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager
                val webView = WebView(context)
                webView.settings.apply {
                    javaScriptEnabled = false
                    defaultTextEncodingName = "utf-8"
                }

                webView.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        try {
                            val width = 595 // A4 width in points
                            val pageHeight = 842 // A4 height in points

                            webView.measure(
                                View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                            )
                            val measuredHeight = webView.measuredHeight.coerceAtLeast(pageHeight)
                            webView.layout(0, 0, width, measuredHeight)

                            val pdfDocument = PdfDocument()
                            val totalPages = Math.ceil(measuredHeight.toDouble() / pageHeight).toInt().coerceAtLeast(1)

                            for (i in 0 until totalPages) {
                                val pageInfo = PdfDocument.PageInfo.Builder(width, pageHeight, i + 1).create()
                                val page = pdfDocument.startPage(pageInfo)
                                val canvas = page.canvas

                                canvas.save()
                                canvas.translate(0f, -i.toFloat() * pageHeight)
                                webView.draw(canvas)
                                canvas.restore()

                                pdfDocument.finishPage(page)
                            }

                            FileOutputStream(outputFile).use { out ->
                                pdfDocument.writeTo(out)
                            }
                            pdfDocument.close()

                            onSuccess(outputFile)
                        } catch (e: Exception) {
                            onError(e)
                        }
                    }
                }

                webView.loadDataWithBaseURL("file:///android_asset/", htmlContent, "text/html", "utf-8", null)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun exportToPdfToUri(
        context: Context,
        htmlContent: String,
        targetUri: Uri,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val tempFile = File(context.cacheDir, "temp_pdf_${System.currentTimeMillis()}.pdf")
        exportToPdf(
            context = context,
            htmlContent = htmlContent,
            outputFile = tempFile,
            onSuccess = { cacheFile ->
                try {
                    context.contentResolver.openOutputStream(targetUri, "w")?.use { out ->
                        cacheFile.inputStream().use { input ->
                            input.copyTo(out)
                        }
                        out.flush()
                    }
                    cacheFile.delete()
                    onSuccess()
                } catch (e: Exception) {
                    onError(e)
                }
            },
            onError = onError
        )
    }
}
