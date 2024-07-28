
package webapp

import viz.WithBaseSpec
import viz.LowPriorityPlotTarget
import ujson.Value


case class Laminarble(val duration:Int, override val mods: Seq[ujson.Value => Unit] = List())
extends  WithBaseSpec(mods: Seq[ujson.Value => Unit])(using viz.PlotTargets.doNothing) :
    override lazy val baseSpec: Value = ujson.read(s"""
        {
  "$$schema": "https://vega.github.io/schema/vega/v5.json",
  "description": "Marble plot",
  "signals": [
    {
      "name": "height",
      "init": 200 
    },
    {
      "name": "width",
      "init": 200
    }
  ],
  "data": [
    {
      "name": "streamline",
      "values": [
        {
          "label": "A",
          "start": 0,
          "end": $duration
          

        },
        {
          "label": "B",
          "start": 0,
          "end": $duration
          
        },
        {
          "label": "C",
          "start": 0,
          "end": $duration
          
        }
      ]
    },
    {
      "name": "events",
      
      "values": [
        { "name":"1", "when":"0.5", "streamline": "A", "target": "C" },
        { "name":"2", "when":"1", "streamline": "B", "target": "C" },
        { "name":"3", "when":"1.5", "streamline": "A", "target": "C" }
      ]
    }

  ],

  "scales": [
    {
      "name": "yscale",
      "type": "band",
      "range": [0, {"signal": "height"}],
      "domain": {"data": "streamline", "field": "label"},
      "padding":0 
    },
    {
      "name": "xscale",
      "type": "time",
      "range": [0, {"signal" :"width"}],
      "round": true,
      "domain": {"data": "streamline", "fields": ["start", "end"]}
    }
  ],

  "marks": [
    
    {
      "type": "text",
      "from": {"data": "events"},
      "encode": {
        "update": {
          "x": {"scale": "xscale", "field": "when"},
          "y": {"value": -10},
          "angle": {"value": -25},
          "fill": {"value": "#000"},
          "text": {"field": "name"},
          "fontSize": {"value": 10}
        }
      }
    },
        {
      "type": "symbol",
      "from": {"data": "streamline"},
      "name" : "timeLazer",
      "encode": {
        "update": {
          "x": {"value": 0},
          "y": {"scale": "yscale", "field":"label"},
          "size": {"value" : 100}, 
          "fill": {"value": "red"},
          "name": {"value": "argy"}
        }
      }
    },
    {
      "type": "rect",
      "from": {"data": "events"},
      "encode": {
        "update": {
          "x": {"scale": "xscale", "field": "when"},
          "y": {"scale": "yscale", "field":"target"},
          "y2": {"field": "streamline", "scale": "yscale"},
          
          "width": {"value": 1},
          
          "fill": {"value": "#888"}
        }
      }
    },
    {
      "type": "symbol",
      "from": {"data": "events"},
      "encode": {
        "update": {
          "x": {"scale": "xscale", "field": "when"},
          "y": {"scale": "yscale", "field":"streamline"},
          "size": {"value" : 2500}
        }
      }
    },
    {
      "type": "text",
      "from": {"data": "streamline"},
      "encode": {
        "update": {
          "x": {"scale": "xscale", "field": "start"},
          "y": {"scale": "yscale", "field": "label", "offset": -3},
          "fill": {"value": "#000"},
          "text": {"field": "label"},
          "fontSize": {"value": 10}
        }
      }
    },
    {
      "type": "rect",
      "from": {"data": "streamline"},
      "encode": {
        "update": {
          "x": {"scale": "xscale", "field": "start"},
          "x2": {"scale": "xscale", "field": "end"},
          "y": {"scale": "yscale", "field": "label"},
          "height": {"value": 2},
          "fill": {"value": "#557"}
        }
      }
    },
    {
      "type": "rect",
      "from": {"data": "streamline"},
      "encode": {
        "update": {
          "x": {"scale": "xscale", "field": "enter"},
          "x2": {"scale": "xscale", "field": "leave"},
          "y": {"scale": "yscale", "field": "label", "offset":-1},
          "height": {"value": 4},
          "fill": {"value": "#e44"}
        }
      }
    }
  ]
}""".trim )