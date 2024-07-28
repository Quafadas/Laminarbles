set shell := ["powershell.exe", "-Command"]

outDir := justfile_directory() + "/.out"
staticDir := justfile_directory() / "static"

setup-ide:
  scala-cli setup-ide .

dev:
  cs launch io.github.quafadas:sjsls_3:0.2.1 -- --build-tool-invocation scala-cli --port 3001 --styles-dir {{staticDir}}

gha:
  scala-cli

buildJs:
  mkdir -p outDir
  scala-cli --power package front/shoelace.scala -o {{outDir}} -f