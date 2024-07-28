package webapp

import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.SVGElement

/* in chart spec, we add this timeLayzer class to some of the elements */
// Create a MutationObserver to watch for changes in the DOM
def makeObserver(
    updater: (List[SVGElement] => List[SVGElement]) => Unit,
    forClass: String
) =
  new dom.MutationObserver((mutations, _) => {
    mutations.foreach { mutation =>
      mutation.addedNodes.foreach { node =>
        // println(s"Element added: ${node.innerText}")
        if (node.isInstanceOf[dom.svg.Element]) {
          val element = node.asInstanceOf[dom.svg.Element]
          // val classList = element.classList.toString
          if (element.classList.contains(forClass)) {
            // Add the element to the Var for later use
            // println(s"Element added: ${element.outerHTML}, Classes: $classList")
            updater(_ :+ element)
          }
        }
      }
    }
  })
