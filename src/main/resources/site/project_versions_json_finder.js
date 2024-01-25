const url = "%%link%%";

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
        const name = element.type;
        if(name==="beta") vrt = {id:"beta",name:"Beta"}
        if(name==="alpha") vrt = {id:"alpha",name:"Alpha"}
        if(name==="prototype") vrt = {id:"prototype",name:"Prototype"}
        if(name==="rc") vrt = {id:"rc",name:"Release Candidate"}
        if(name==="snapshot") vrt = {id:"snapshot",name:"Snapshot"}
        if(name==="pre_release") vrt = {id:"pre_release",name:"Pre-Release"}

        const date = new Date();
        date.setTime(Date.parse(element.release_date));


        versions.push(
            `
            <div class="entry">
                <div>
                    <span class="title version-tag ${vrt.id}">&#x25CF; ${vrt.name}</span>
                    <span class="title">${element.version}</span>
                </div>
                <div>
                    <img src="../../images/icon/clock.svg" alt="Clock Icon" width="20" style="vertical-align: middle;" /><span class="alt">${date.getDate()} of ${months[date.getMonth()]}, ${date.getFullYear()}</span>
                </div>
            </div>
            `
        );


        elementt.innerHTML = versions.join("\n\n");
    })
}).then(()=>{
    Prism.highlightAll();
});