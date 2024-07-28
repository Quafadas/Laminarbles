
package webapp

import animus.*

import org.scalajs.dom
import org.scalajs.dom.document
import com.raquo.laminar.api.L.{*, given}
import viz.vega.plots.PieChart
import viz.vega.plots.PieChartLite
import viz.LaminarViz
import viz.vega.facades.EmbedOptions
import org.w3c.dom.html.HTMLCollection
import scala.scalajs.js
import com.raquo.laminar.nodes.ReactiveSvgElement
import org.scalajs.dom.SVGElement
// import com.raquo.laminar.api.L.svg.*

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


  val rocketSignal =
    EventStream
      .periodic(50)
      .toSignal(0)
      .map(_ => 10)
      .foldLeft(_ => 0.0) { (curr, next) =>
            if (curr + next) > 300 then 0 else curr + next
      }
  
  val mounted = Var[Boolean](false)
  trait StreamlineJS extends js.Object {
    var label : String
    var start : Int
    var duration : Int
  } 

  case class Streamline(
    label :String,
    start: Int,
    duration: Int
  )

  object StreamlineJS {
  def apply(label: String, start: Int, duration: Int): StreamlineJS = {
    new StreamlineJS {
      var label: String = label
      var start: Int = start
      var duration: Int = duration
    }
  }
  def ujsonObj(label: String, start: Int, duration: Int) = 
    ujson.Obj(
      "label" -> label, 
      "start" -> start,
      "duration" -> duration
    )
}
  val objs = ujson.Arr(
    StreamlineJS.ujsonObj("A",0,5),
    StreamlineJS.ujsonObj("B",0,5),
    StreamlineJS.ujsonObj("C",0,5)
  )  

  val rocket = div(
        div(
          s"🚀",
          transform := "rotate(40deg)",
          transformOrigin := "top left"
        ),
        // fontSize := "2em",
        // transformOrigin("left"),
        paddingLeft <-- rocketSignal
          .spring
          .px
      )

  val timeLazerElements = Var[List[dom.svg.Element]](List.empty)

  // Create a MutationObserver to watch for changes in the DOM
  val observer = new dom.MutationObserver((mutations, _) => {
    mutations.foreach { mutation =>
      mutation.addedNodes.foreach { node =>
        if (node.isInstanceOf[dom.svg.Element]) {
          val element = node.asInstanceOf[dom.svg.Element]
          val classList = element.classList.toString
          // println(s"Element added: ${element.outerHTML}, Classes: $classList")
          if (element.classList.contains("timeLazer")) {
            // Add the element to the Var for later use
            timeLazerElements.update(_ :+ element)
          }
        }
      }
    }
  })

  // Start observing the document body for changes
  

  val chart = LaminarViz.simpleEmbed(

        Laminarble2(
          2,
          List(
            spec => spec("data")(0)("values") = objs
            // viz.Utils.fillDiv,
            // spec => spec("marks")(0)("name") = "piedy"
          )
        ),
        embedOpt = Some(
          EmbedOptions(renderer = "svg")
        )
      ).amend(
        width := "98vw",
        height := "80vh",
        cls  := "thisone"
      )

      
  observer.observe(chart.ref, dom.MutationObserverInit(childList = true, subtree = true))

  div(
    rocket,
    div(
      chart,
        // Example usage: Print the elements stored in the Var whenever it changes
      timeLazerElements.signal.changes.delay(1000).map { elements =>
        
        elements.foreach { element =>
          // println(js.JSON.stringify( element.childNodes))
          println(element.childNodes.length)
          element.childNodes.foreach(dot =>
            val typed = dot.asInstanceOf[org.scalajs.dom.svg.Path]
            val el = foreignSvgElement(typed)
            println("spring")
            // typed.setAttribute("transform", "translate(100,100)") // works
            el.amend(
              // com.raquo.laminar.api.L.svg.fill := "green", //works  
              com.raquo.laminar.api.L.svg.fill <-- EventStream.fromValue("green").delay(1000),
              com.raquo.laminar.api.L.svg.transform <-- rocketSignal.spring.debugLog().map{m =>
                println(m)
                s"translate(0,$m)"
              }
            )
          )
          
        }
      } --> Observer {_ => ()}, 
      child <-- EventStream.fromValue(  p("Test") )
    ),
    div(
      // transformOrigin("left"),
      width := "100%",
      
    )
  )
