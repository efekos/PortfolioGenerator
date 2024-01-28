/*
 * Copyright 2024 efekos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the “Software”), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package dev.efekos.pg.process;

import dev.efekos.pg.data.timeline.TimelineEvent;
import dev.efekos.pg.data.timeline.TimelineEventSource;

import java.util.ArrayList;
import java.util.List;

public class CollectTimelineEventsProcess implements Process{
    @Override
    public String getName() {
        return "Collect Timeline Events";
    }

    @Override
    public void init(ProcessContext context) throws Exception {
        List<Object> everything = context.getEverything();

        List<TimelineEvent> collectedEvents = new ArrayList<>();

        for (Object o : everything) {
            System.out.println("Looking for a "+o.getClass().getName());
            if(o instanceof TimelineEventSource source){
                System.out.println("Found a TimelineEventSource");
                collectedEvents.addAll(source.getEvents());
            } else if (o instanceof List<?> list){
                System.out.println("Found a TimelineEventSource in "+list.getClass().getName());
                if(list.get(0) instanceof TimelineEventSource source){
                    collectedEvents.addAll(source.getEvents());
                }
            }
        }

        context.collectedTimeline = collectedEvents;
    }
}
