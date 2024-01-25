const url = "https://api.github.com/repos/%%repo%%/releases?per_page=15";



const elementt = document.getElementById("releases");
const curPageElement = document.getElementById("cur");
const nextPageElement = document.getElementById("nextBtn");
const prevPageElement = document.getElementById("prevBtn");
const lastPageElement = document.getElementById("lastBtn");
const firstPageElement = document.getElementById("firstBtn");

const months = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December"
];

let prevPage = 0;
let nextPage = 2;
const firstPage = 1;
let lastPage = 3;


function splitLinkHeader(linkHeader) {

    const things = linkHeader.split(",");
    return things.map(s => {
        const part = s.split(";");

        const l = part[0].replace(" ", "").substring(1);
        return {
            link: l.substring(0, l.length - 1),
            rel: part[1].substring(6, 11).replace('"', '')
        };
    });

}


function getPageNumber(lin) {
    let i = lin;

    while (!i.startsWith("page=")) {
        i = i.substring(1);
    }

    return Number.parseInt(i.substring(13));
}

function refreshPage(page) {
    if (typeof page !== "number") throw new TypeError("page must be a number");
    elementt.innerHTML = `
      <div class="entry entry-placeholder" style="min-height: 5rem;"></div>
                            <div class="entry entry-placeholder" style="min-height: 5rem;"></div>
                            <div class="entry entry-placeholder" style="min-height: 3rem;"></div>
                            <div class="entry entry-placeholder" style="min-height: 4rem;"></div>
                            <div class="entry entry-placeholder" style="min-height: 5rem;"></div>
                            <div class="entry entry-placeholder" style="min-height: 4.5rem;"></div>
    `;

    const versions = [];


    fetch(url + `&page=${page}`).then(res => {
        return res.json().then(body => { return { body, headers: res.headers }; });
    }).then(res => {
        const linkHeader = res.headers.get("Link");

        const splitted = splitLinkHeader(linkHeader);
        lastPage = -1;
        nextPage = -1;
        prevPage = -1;

        splitted.forEach(sp => {
            if (sp.rel === "last") lastPage = getPageNumber(sp.link);
            if (sp.rel === "prev") prevPage = getPageNumber(sp.link);
            if (sp.rel === "next") nextPage = getPageNumber(sp.link);
        });

        if (nextPage != -1) curPageElement.innerText = nextPage - 1 + " / " + (lastPage==-1?nextPage-1:lastPage);
        if (prevPage != -1) curPageElement.innerText = prevPage + 1 + " / " + (lastPage==-1?prevPage+1:lastPage);
        nextPageElement.disabled = nextPage == -1;
        prevPageElement.disabled = prevPage == -1;
        firstPageElement.disabled = nextPage == 2;
        lastPageElement.disabled = lastPage==-1;

        res.body.forEach(element => {

            var vrt = res.body.prerelease ? { id: "prerelease", name: "Pre-Release" } : { id: "release", name: "Release" };
            const name = element.name.toLowerCase();
            const tagName = element.tag_name.toLowerCase();
            if (name.includes("beta")||tagName.includes("beta")) vrt = { id: "beta", name: "Beta" };
            if (name.includes("alpha")||tagName.includes("alpha")) vrt = { id: "alpha", name: "Alpha" };
            if (name.includes("prototype")||tagName.includes("prototype")) vrt = { id: "prototype", name: "Prototype" };
            if (name.includes("release candidate")||tagName.includes("release candidate")) vrt = { id: "rc", name: "Release Candidate" };
            if (name.includes("snapshot")||tagName.includes("snapshot")) vrt = { id: "snapshot", name: "Snapshot" };

            const date = new Date();
            date.setTime(Date.parse(element.published_at));


            versions.push(
                `
                <div class="entry">
                    <div>
                        <span class="title version-tag ${vrt.id}">&#x25CF; ${vrt.name}</span>
                        <span class="title">${element.tag_name}</span>
                    </div>
                    <div>
                        <img src="../../images/icon/clock.svg" alt="Clock Icon" width="20" style="vertical-align: middle;" /><span class="alt">${date.getDate()} of ${months[date.getMonth()]}, ${date.getFullYear()}</span>
                    </div>
                    <div>
                        <a href="${element.html_url}" target="_blank"><button class="btn btn-download">See</button></a>
                    </div>
                </div>
                `
            );


            elementt.innerHTML = versions.join("\n\n");
        });
    });

}

refreshPage(1);