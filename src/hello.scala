package webapp

import animus.*

import org.scalajs.dom
import org.scalajs.dom.document
import com.raquo.laminar.api.L.{*, given}

import viz.vega.facades.EmbedOptions
import org.w3c.dom.html.HTMLCollection
import scala.scalajs.js
import com.raquo.laminar.nodes.ReactiveSvgElement
import org.scalajs.dom.SVGElement
import viz.vega.facades.embed
import org.scalajs.dom.HTMLElement
// import com.raquo.laminar.api.L.svg.*

def app =
  val timeLazerElements = Var[List[dom.svg.Element]](List.empty)

  // It is assumed this class is added to the SVG elements in the spec.
  makeObserver(timeLazerElements.update, "timeLazer").observe(
    dom.document,
    dom.MutationObserverInit(childList = true, subtree = true)
  )

  val spec = FetchStream
    .get("marble.vg.json")
    .map(js.JSON.parse(_).asInstanceOf[js.Object])

  def chart(in: HTMLElement) = spec
    .flatMapSwitch { spec =>
      val opts = EmbedOptions(
        renderer = "svg"
      )
      EventStream.fromJsPromise(
        viz.vega.facades.embed(in, spec, opts)
      )
    }
    .map { er =>
      foreignHtmlElement(er.view.container().asInstanceOf[dom.html.Element])
    }

  div(
    h1("Playing"),
    div(
      inContext(thisN => child <-- chart(thisN.ref))
    ),
    timeLazerElements.signal.changes.delay(500).map { elements =>
      elements.foreach { element =>
        // println(js.JSON.stringify( element.childNodes))
        // println(element.childNodes.length)
        element.childNodes.foreach(dot =>
          val typed = dot.asInstanceOf[org.scalajs.dom.svg.Path]
          val el = foreignSvgElement(typed)
          println("each dot, set green")
          // typed.setAttribute("transform", "translate(100,100)") // works
          el.amend(
            /** This is the part, that I don't understand.
              */

            // com.raquo.laminar.api.L.svg.fill := "green",

            /** Theline above works, but this one not...
              */
            com.raquo.laminar.api.L.svg.fill <-- EventStream
              .fromValue("green")

            /** Here was my plan...
              */

            // com.raquo.laminar.api.L.svg.transform <-- rocketSignal.spring
            //   .map { m =>
            //     s"translate(0,$m)"
            // }
          )
        )
      }
    } --> Observer { _ => () }
  )
