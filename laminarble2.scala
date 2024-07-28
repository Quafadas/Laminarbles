
package webapp

import viz.WithBaseSpec
import viz.LowPriorityPlotTarget
import ujson.Value
import viz.vega.plots.SpecUrl
import org.scalajs.dom.XMLHttpRequest

case class Laminarble2(val duration:Int, override val mods: Seq[ujson.Value => Unit] = List())
extends  WithBaseSpec(mods: Seq[ujson.Value => Unit])(using viz.PlotTargets.doNothing) :
    override lazy val baseSpec: Value =      
      val xhr = new XMLHttpRequest()
      xhr.open("GET", "marble.vg.json", false)
      xhr.send()
      ujson.read(xhr.responseText)