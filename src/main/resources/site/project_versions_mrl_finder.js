const url = "https://api.modrinth.com/v2/project/%%id%%/version";

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


fetch(url,{headers:{'User-Agent':"efekos/PortfolioGenerator/1.0.0"}}).then(res=>{
    return res.json();
}).then(res => {
    res.forEach(element => {

        let vrt = {id: "release", name: "Release"};
        const name = element.name.toLowerCase();
        if(name.includes("pre release")) vrt = {id:"pre_release",name: "Pre-Release"}
        if(name.includes("beta")) vrt = {id:"beta",name:"Beta"}
        if(name.includes("alpha")) vrt = {id:"alpha",name:"Alpha"}
        if(name.includes("prototype")) vrt = {id:"prototype",name:"Prototype"}
        if(name.includes("release candidate")) vrt = {id:"rc",name:"Release Candidate"}
        if(name.includes("snapshot")) vrt = {id:"snapshot",name:"Snapshot"}

        switch (element.version_type){
            case "release": vrt = {id:"release",name: "Release"};break;
            case "beta": vrt = {id:"beta",name: "Beta"};break;
            case "alpha": vrt = {id:"alpha",name: "Alpha"};break;
        }

        const date = new Date();
        date.setTime(Date.parse(element.date_published));


        versions.push(
            `
            <div class="entry">
                <div>
                    <span class="title version-tag ${vrt.id}">&#x25CF; ${vrt.name}</span>
                    <span class="title">${element.name}</span>
                </div>
                <div>
                    <img src="../../images/icon/clock.svg" alt="Clock Icon" width="20" style="vertical-align: middle;" /><span class="alt">${date.getDate()} of ${months[date.getMonth()]}, ${date.getFullYear()}</span>
                </div>
                <div>
                    <a href="https://modrinth.com/mod/%%id%%/version/${element.id}" target="_blank"><button class="btn btn-download">See More</button></a>
                </div>
            </div>
            `
        );


        elementt.innerHTML = versions.join("\n\n");
    });
}).then(()=>{
    Prism.highlightAll();
});;