document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('locales').addEventListener('change', function () {
        const selectedLang = this.value;
        console.log("Selected Language: " + selectedLang);

        if (selectedLang) {
            localStorage.setItem('language', selectedLang);
            console.log("Saved language to localStorage: " + selectedLang);
            window.location.href = window.location.pathname + '?lang=' + selectedLang;
        }
    });

    const savedLang = localStorage.getItem('language');
    if (savedLang) {
        document.getElementById('locales').value = savedLang;
        console.log("Restored language from localStorage: " + savedLang);
    }
});
