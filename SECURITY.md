# Security Boundary

Sky PDF Core renders only bounded printable-ASCII strings into a small deterministic single-page PDF. It does not execute templates, scripts, HTML, external commands, URLs, fonts, or user-supplied binary assets.

The CLI refuses to overwrite an existing output path. Callers remain responsible for selecting an authorized destination directory and for applying filesystem permissions appropriate to the document contents.

The renderer is not a PDF sanitizer, malware scanner, cryptographic signer, encryption system, archival validator, or accessibility validator. Generated documents should not be represented as PDF/A, signed, encrypted, or independently security-reviewed.
