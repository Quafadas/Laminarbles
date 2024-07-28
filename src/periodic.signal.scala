package webapp 

import org.scalajs.dom
import com.raquo.airstream.core.EventStream
import com.raquo.airstream.core.Signal

/* for experimenting with animmus */
val rocketSignal: Signal[Double] = EventStream
    .periodic(50)
    .toSignal(0)
    .map(_ => 10)
    .foldLeft(_ => 0.0) { (curr, next) =>
        if (curr + next) > 300 then 0 else curr + next
    }