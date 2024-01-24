const readmeElement = document.getElementById("versions");

const readmeUrl = "%%link%%";

fetch(readmeUrl).then(function (response) {
    return response.text();
}).then(function (data) {

    readmeElement.innerHTML = marked.parse(data);
}).catch(function (err) {
    console.log('Fetch Error :-S', err);
});