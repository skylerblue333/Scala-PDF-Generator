package skycoin4444.pdf

import java.nio.file.{Files, Path}

object Main:
  def main(args: Array[String]): Unit =
    if args.length < 2 then
      System.err.println("usage: sky-pdf <output.pdf> <title> [line ...]")
      sys.exit(2)

    val output = Path.of(args(0)).toAbsolutePath.normalize()
    if Files.exists(output) then
      System.err.println("refusing to overwrite existing output")
      sys.exit(3)

    val document = SkyPdf.Document(args(1), args.drop(2).toVector)
    SkyPdf.render(document) match
      case Left(error) =>
        System.err.println(error)
        sys.exit(4)
      case Right(bytes) =>
        Option(output.getParent).foreach(parent => Files.createDirectories(parent))
        Files.write(output, bytes)
        println(s"wrote ${bytes.length} bytes to $output")
