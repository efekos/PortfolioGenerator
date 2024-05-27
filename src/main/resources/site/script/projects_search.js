/*
 * Copyright 2024 efekos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the �Software�), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED �AS IS�, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

const elements = document.getElementsByClassName("searchable");
const data = [];
const allTags = [];
const tagFiltersElement = document.getElementById("tagFilters");


// collecting data
for (let i = 0; i < elements.length; i++) {
    const element = elements[i];

    const title = element.getElementsByClassName("title")[0].innerText;
    const tagElements = element.getElementsByClassName("tagsTag")[0].getElementsByTagName("div");
    const tags = [];
    for (let j = 0; j < tagElements.length; j++) {
        const element = tagElements[j];
        tags.push(element.innerText);
    }

    data.push({title, tags, element});
}

// collecting tags
data.map(r => r.tags).forEach(tags => {
    tags.forEach(tag => {
        if (!allTags.includes(tag)) allTags.push(tag);
    });
});

// generating tag filters
const tagElements = [];
allTags.forEach(tag => {
    tagElements.push(`<input type="checkbox" class="filter-tag" name="filter-tag-${tag}" id="filter-tag-${tag}" onchange="onFilterChange(event)"><label for="filter-tag-${tag}">${tag}</label>`);
});
tagFiltersElement.innerHTML = tagElements.join("<br>");


function onInputChange(e) {
    doSearch(e.target.value);
}

function bothIncludes(s1, s2) {
    return s1.includes(s2) || s2.includes(s1);
}

let hasTagFiltering = false;
let selectedTags = [];

function onFilterChange(event) {
    const elements = document.getElementsByClassName("filter-tag");
    hasTagFiltering = false;
    for (let i = 0; i < elements.length; i++) {
        const element = elements[i];

        hasTagFiltering = hasTagFiltering || element.checked;
        if (hasTagFiltering) break;
    }

    doSearch(document.getElementById("search").value);
    if (event.target.checked) selectedTags.push(event.target.id.substring(11)); else selectedTags = selectedTags.filter(r => r != event.target.id.substring(11));
}

function doSearch(v) {
    if (hasTagFiltering) {
        data.forEach((dat) => {
            const hasFilteredTag = dat.tags.find(r => document.getElementById(`filter-tag-${r}`).checked) != undefined;
            dat.element.hidden = !hasFilteredTag;
        });
    } else {
        const tag = selectedTags[0]
        data.forEach(dat => {
            if (!dat.tags.includes(tag))// means it should not be hidden but is hidden
                dat.element.hidden = false;
        })
    }

    if (v.replaceAll(" ", "") === "") return;
    data.forEach((dat) => {
        if (!dat.element.hidden) {
            const titleMatches = bothIncludes(dat.title.toLowerCase(), v);
            dat.element.hidden = !titleMatches;
        }
    });
}