const url = "https://api.github.com/repos/%%repo%%/releases";

const versions = [];

const elementt = document.getElementById("releases");

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

fetch(url).then(res => {
    return res.json();
}).then(res => {
    console.log(res);

    res.forEach(element => {

        var vrt = res.prerelease ? { id: "prerelease", name: "Pre-Release" } : { id: "release", name: "Release" };
        const name = element.name.toLowerCase();
        if(name.includes("beta")) vrt = {id:"beta",name:"Beta"}
        if(name.includes("alpha")) vrt = {id:"alpha",name:"Alpha"}
        if(name.includes("prototype")) vrt = {id:"prototype",name:"Prototype"}
        if(name.includes("release candidate")) vrt = {id:"rc",name:"Release Candidate"}
        if(name.includes("snapshot")) vrt = {id:"snapshot",name:"Snapshot"}

        const date = new Date();
        date.setTime(Date.parse(element.published_at));


        versions.push(
            `
            <div class="entry">
                <div>
                    <span class="title version-tag ${vrt.id}">● ${vrt.name}</span>
                    <span class="title">${element.tag_name}</span>
                </div>
                <div>
                    <img src="../../images/icon/clock.svg" alt="Clock Icon" width="20" style="vertical-align: middle;" /><span class="alt">${date.getDate()} of ${months[date.getMonth()]}, ${date.getFullYear()}</span>
                </div>
                <div>
                    <a href="${element.html_url}" target="_blank"><button class="btn btn-download">See More</button></a>
                </div>
            </div>
            `
        );


        elementt.innerHTML = versions.join("\n\n");
    });
});