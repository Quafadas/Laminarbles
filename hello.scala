//> using scala 3.4.2
//> using platform js

//> using dep org.scala-js::scalajs-dom::2.8.0
//> using dep com.raquo::laminar::17.0.0
//> using dep io.github.quafadas::dedav_laminar::0.9.3
//> using dep io.github.kitlangton::animus::0.6.2

//> using jsEmitSourceMaps true
//> using jsModuleKind es
//> using jsModuleSplitStyleStr smallmodulesfor
//> using jsSmallModuleForPackage webapp
//> using jsEsModuleImportMap importmap.json

package webapp

import animus.*
import org.scalajs.dom
import org.scalajs.dom.document
import com.raquo.laminar.api.L.{*, given}
import viz.vega.plots.PieChart
import viz.vega.plots.doNothing
import viz.vega.plots.PieChartLite
import viz.LaminarViz
import viz.vega.facades.EmbedOptions

@main
def main: Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    app
  )

def app =
  type modStream = EventStream[String] => EventStream[String]
  def modSignal: (EventStream[String] => EventStream[String]) =
    (es: EventStream[String]) => es.delay(1000)

  // start at the given index, then take the next 3 elements, rotating back to the start
  def rotate[A](start: Int, take: Int, as: Vector[A]): Vector[A] =
    val size = as.size
    val i = start % size
    val j = (i + take) % size
    if j < i then as.slice(i, size) ++ as.slice(0, j)
    else as.slice(i, j)

  val rocketSignal =
    EventStream
      .periodic(50)
      .toSignal(0)
      .map(_ => 10)

  val hiVar = Var("Scala JS") // Local state
  div(
    div(
      // transformOrigin("left"),
      width := "100%",
      div(
        div(
          s"🚀",
          transform := "rotate(40deg)",
          transformOrigin := "top left"
        )
        // fontSize := "2em",
        // transformOrigin("left"),
        // paddingLeft <-- rocketSignal
        //   .foldLeft(_ => 0.0) { (curr, next) =>
        //     if (curr + next) > 300 then 0 else curr + next
        //   }
        //   .spring
        //   .px,
        // height := "100px"

        // .map { s =>
        //   println(s)
        //   s"scale(${s})"

        // }
      )
    ),
    h1(
      s"Hello ",
      child.text <-- hiVar.signal
    ),
    p("This page should reload on change  Check the justfile... asdf  "),
    // https://demo.laminar.dev/app/form/controlled-inputs
    input(
      typ := "text",
      controlled(
        value <-- hiVar.signal,
        onInput.mapToValue --> hiVar.writer
      )
    ),
    div(
      width := "50vmin",
      height := "50vmin",
      LaminarViz.simpleEmbed(
        PieChart(
          List(
            viz.Utils.fillDiv,
            spec => spec("marks")(0)("name") = "piedy"
          )
        ),
        // embedOpt = Some(
          // EmbedOptions(renderer = "svg")
        // )
      )
    )
  )
