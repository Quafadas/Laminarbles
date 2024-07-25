outDir := justfile_directory() + "/.out"

setup-ide:
  scala-cli setup-ide .

dev:
  cs launch --contrib sjsls

gha:
  scala-cli

buildJs:
  mkdir -p outDir
  scala-cli --power package front/shoelace.scala -o {{outDir}} -f