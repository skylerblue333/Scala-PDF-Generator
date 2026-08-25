package skycoin4444.pdf

import java.nio.charset.StandardCharsets

object SkyPdf:
  final case class Document(title: String, lines: Vector[String])

  private val MaxTitle = 120
  private val MaxLines = 40
  private val MaxLine = 100

  def render(document: Document): Either[String, Array[Byte]] =
    validate(document).map { doc =>
      val content = contentStream(doc)
      val objects = Vector(
        "<< /Type /Catalog /Pages 2 0 R >>",
        "<< /Type /Pages /Kids [3 0 R] /Count 1 >>",
        "<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] /Resources << /Font << /F1 5 0 R >> >> /Contents 4 0 R >>",
        s"<< /Length ${content.getBytes(StandardCharsets.US_ASCII).length} >>\nstream\n$content\nendstream",
        "<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>"
      )

      val header = "%PDF-1.4\n%SkyPDF\n"
      val builder = new StringBuilder(header)
      val offsets = objects.zipWithIndex.map { case (body, index) =>
        val offset = builder.toString.getBytes(StandardCharsets.US_ASCII).length
        builder.append(s"${index + 1} 0 obj\n$body\nendobj\n")
        offset
      }
      val xrefOffset = builder.toString.getBytes(StandardCharsets.US_ASCII).length
      builder.append(s"xref\n0 ${objects.length + 1}\n")
      builder.append("0000000000 65535 f \n")
      offsets.foreach(offset => builder.append(f"$offset%010d 00000 n \n"))
      builder.append(s"trailer\n<< /Size ${objects.length + 1} /Root 1 0 R >>\nstartxref\n$xrefOffset\n%%EOF\n")
      builder.toString.getBytes(StandardCharsets.US_ASCII)
    }

  private def validate(document: Document): Either[String, Document] =
    val title = document.title.trim
    if title.isEmpty then Left("title must not be empty")
    else if title.length > MaxTitle then Left("title is too long")
    else if document.lines.length > MaxLines then Left("too many lines")
    else if (title +: document.lines).exists(value => !value.forall(ch => ch >= ' ' && ch <= '~')) then
      Left("only printable ASCII is supported")
    else if document.lines.exists(_.length > MaxLine) then Left("line is too long")
    else Right(document.copy(title = title))

  private def contentStream(document: Document): String =
    val escapedTitle = escape(document.title)
    val title = s"BT /F1 18 Tf 72 740 Td ($escapedTitle) Tj ET"
    val body = document.lines.zipWithIndex.map { case (line, index) =>
      val y = 710 - index * 16
      s"BT /F1 11 Tf 72 $y Td (${escape(line)}) Tj ET"
    }
    (title +: body).mkString("\n")

  private def escape(value: String): String =
    value.replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)")
