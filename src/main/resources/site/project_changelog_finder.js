const changeLog = document.getElementById("changelog");

const readmeUrl = "%%link%%";

fetch(readmeUrl).then(function (response) {
    return response.text();
}).then(function (data) {

    changeLog.innerHTML = marked.parse(data);
}).catch(function (err) {
    console.log('Fetch Error :-S', err);
});