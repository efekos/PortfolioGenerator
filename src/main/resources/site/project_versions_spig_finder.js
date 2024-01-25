const url = "https://api.spiget.org/v2/resources/%%id%%/updates?fields=date%2Ctitle&sort=-date&size=100";

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

    res.forEach(element => {

        var vrt = { id: "release", name: "Release" };
        const name = element.title.toLowerCase();
        if(name.includes("beta")) vrt = {id:"beta",name:"Beta"}
        if(name.includes("alpha")) vrt = {id:"alpha",name:"Alpha"}
        if(name.includes("prototype")) vrt = {id:"prototype",name:"Prototype"}
        if(name.includes("release candidate")) vrt = {id:"rc",name:"Release Candidate"}
        if(name.includes("snapshot")) vrt = {id:"snapshot",name:"Snapshot"}
        if(name.includes("pre release")) vrt = {id:"pre_release",name: "Pre-Release"}

        const date = new Date();
        date.setTime(element.date*1000);


        versions.push(
            `
            <div class="entry">
                <div>
                    <span class="title version-tag ${vrt.id}">&#x25CF; ${vrt.name}</span>
                    <span class="title">${element.title}</span>
                </div>
                <div>
                    <img src="../../images/icon/clock.svg" alt="Clock Icon" width="20" style="vertical-align: middle;" /><span class="alt">${date.getDate()} of ${months[date.getMonth()]}, ${date.getFullYear()}</span>
                </div>
                <div>
                    <a href="https://www.spigotmc.org/resources/%%id%%/update?update=${element.id}" target="_blank"><button class="btn btn-download">See More</button></a>
                </div>
            </div>
            `
        );


        elementt.innerHTML = versions.join("\n\n");
    })
}).then(()=>{
    Prism.highlightAll();
});