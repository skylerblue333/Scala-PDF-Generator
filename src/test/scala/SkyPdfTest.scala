package skycoin4444.pdf

import java.nio.charset.StandardCharsets

class SkyPdfTest extends munit.FunSuite:
  test("renders a bounded PDF with xref and EOF") {
    val bytes = SkyPdf.render(SkyPdf.Document("Sky Report", Vector("hello", "world"))).toOption.get
    val text = new String(bytes, StandardCharsets.US_ASCII)
    assert(text.startsWith("%PDF-1.4"))
    assert(text.contains("/BaseFont /Helvetica"))
    assert(text.contains("xref"))
    assert(text.endsWith("%%EOF\n"))
  }

  test("escapes PDF string metacharacters") {
    val bytes = SkyPdf.render(SkyPdf.Document("A (B)", Vector("C\\D"))).toOption.get
    val text = new String(bytes, StandardCharsets.US_ASCII)
    assert(text.contains("A \\(B\\)"))
    assert(text.contains("C\\\\D"))
  }

  test("rejects unsupported Unicode and oversized documents") {
    assertEquals(
      SkyPdf.render(SkyPdf.Document("Sky 🌌", Vector.empty)).swap.toOption,
      Some("only printable ASCII is supported")
    )
    assertEquals(
      SkyPdf.render(SkyPdf.Document("Sky", Vector.fill(41)("line"))).swap.toOption,
      Some("too many lines")
    )
  }
