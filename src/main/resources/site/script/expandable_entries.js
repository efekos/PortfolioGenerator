function toggle(event) {
    if (!event.target.className.includes("entry-toggleable")) return;
    const array = event.target.getElementsByClassName("toggle")
    for (let i = 0; i < array.length; i++) {
        const element = array[i];
        element.hidden = !element.hidden;

        const e = element.getElementsByTagName("span")[0];
        if (e != undefined) e.hidden = !e.hidden
    }

    event.target.getElementsByClassName("entry-expand-icon")[0].src = `./images/icon/${!array[0].hidden ? "un" : ""}expanded.svg`;
}