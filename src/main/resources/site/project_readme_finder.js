const readmeElement = document.getElementById("readme");

const readmeUrl = "%%link%%";

fetch(readmeUrl).then(function (response) {
    return response.text();
}).then(function (data) {

    readmeElement.innerHTML = marked.parse(data);
}).then(()=>{
    Prism.highlightAll();
}).catch(function (err) {
    console.log('Fetch Error :-S', err);
});