console.log("AREP Microframework loaded successfully");

function goHello() {
    const name = document.getElementById("nameInput").value;

    if (!name) {
        alert("Please enter a name");
        return;
    }

    window.location.href = `/greeting?name=${encodeURIComponent(name)}`;
}