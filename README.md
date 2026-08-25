# Sky PDF Core

Sky PDF Core is a focused Scala 3 engineering-beta library and CLI that renders a bounded single-page PDF directly from printable ASCII text.

## Implemented

- genuine Scala 3 implementation
- valid `%PDF-1.4` document structure
- calculated object offsets and cross-reference table
- built-in Helvetica font reference
- title plus up to 40 body lines
- PDF string escaping for backslash and parentheses
- bounded printable-ASCII input validation
- refusal to overwrite an existing CLI output file
- MUnit tests and real CLI smoke generation

## Usage

```bash
sbt 'run report.pdf "Sky Report" "first line" "second line"'
```

The command refuses to overwrite an existing destination.

## Product boundary

Status: **engineering beta**.

This is deliberately a small deterministic PDF renderer, not a complete publishing engine. It does not support Unicode fonts, images, HTML/CSS, multi-page layout, tables, forms, encryption, digital signatures, PDF/A, accessibility tagging, arbitrary font embedding, templates, or server-side rendering. It should not be represented as a replacement for PDFBox, iText, browser print engines, or commercial document systems.

The repository currently ships as a Scala library/CLI. The obsolete Python/Uvicorn container was removed rather than claiming a verified JVM container before one exists.

## Verification

CI runs Scala compilation, MUnit tests, then generates a real PDF and verifies its PDF header.

## SKYCOIN4444 integration

Use this component for small deterministic text reports where the bounded ASCII/single-page contract is sufficient. Route richer document requirements to a separately verified rendering service.
