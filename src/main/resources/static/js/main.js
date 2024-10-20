window.onload = function () {
    setTimeout(function () {
        console.log("vao dta");
        var toastElement = document.getElementById("toasts");
        console.log(toastElement);
        if (toastElement) {
            toastElement.style.display = "none";
        }
    }, 3000);
};
