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

import dev.efekos.pg.Main;
import dev.efekos.pg.data.timeline.TimelineEvent;
import dev.efekos.pg.data.timeline.TimelineEventSource;

import javax.naming.Name;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CollectTimelineEventsProcess implements Process{
    @Override
    public String getName() {
        return "Collect Timeline Events";
    }

    private static final List<String> EXCLUDED_FIELDS = Arrays.asList("collectedTimeline");

    @Override
    public void init(ProcessContext context) throws Exception {
        List<TimelineEvent> collectedEvents = new ArrayList<>();

        Main.LOGGER.info("Starting search");

        Main.DEBUG_LOGGER.info("Getting fields");
        Field[] fields = context.getClass().getFields();

        for (Field field : Arrays.asList(fields)) {

            Main.DEBUG_LOGGER.info("Searching field: ProcessContext."+field.getName());
            if(EXCLUDED_FIELDS.contains(field.getName())) {
                Main.DEBUG_LOGGER.error("Field is excluded. Skipping");
                continue;
            }

            Object o = field.get(context);

            if(o instanceof TimelineEventSource source){
                Main.DEBUG_LOGGER.success("Found a TimelineEventSource: "+field.getName());
                Main.DEBUG_LOGGER.success("Collecting events on ",field.getName());
                collectedEvents.addAll(source.getEvents());
            } else if (o instanceof List<?> list && list.size()>0 && list.get(0) instanceof TimelineEventSource){
                Main.DEBUG_LOGGER.success("Found a list of TimelineEventSource: "+field.getName());
                Main.DEBUG_LOGGER.success("Collecting events on ",field.getName());

                list.forEach(o1 -> collectedEvents.addAll(((TimelineEventSource) o1).getEvents()));
            }

        }


        Main.LOGGER.success("Finished search with ",collectedEvents.size()+" events found");

        context.collectedTimeline = collectedEvents;
    }
}
