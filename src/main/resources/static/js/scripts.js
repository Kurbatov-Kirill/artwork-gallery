    let isPickingModeActive = false;
    let isOrderEditModeActive = false;
    let pickedImages = [];
    let imagesOrder = [];

    function togglePickingMode() {
        isPickingModeActive = !isPickingModeActive;
        if (isPickingModeActive) {
        const elems = document.getElementsByClassName("checkbox");
        const markElems = document.getElementsByClassName("marking");

            for (let i = 0; i < elems.length; i++) {
                elems[i].style.display = 'block';
                console.log(elems[i].parentNode);
            }

            for (let i = 0; i < markElems.length; i++) {
                markElems[i].style.display = 'inline-block';
            }
        } else {
            const elems = document.getElementsByClassName("checkbox");
            const markElems = document.getElementsByClassName("marking");

            for (let i = 0; i < elems.length; i++) {
                if(elems[i].classList.contains("picked")) {
                    elems[i].classList.remove("picked");
                }
                elems[i].style.display = 'none'
                console.log(elems[i].parentNode);
            }

            for (let i = 0; i < markElems.length; i++) {
                markElems[i].style.display = 'none';
            }

            pickedImages.length = 0
        }
    }


    function pickElem(e, image, container) {
        if (isPickingModeActive) {
            event.preventDefault();
            let list = document.getElementById("picked_img_list");

            let checkbox = container.querySelector('.checkbox');
            console.log(image.id);
            if (checkbox && !checkbox.classList.contains("picked")) {
                checkbox.classList.add("picked");
                pickedImages.push(image.id);
                list.value = pickedImages;
            } else if (checkbox && checkbox.classList.contains("picked")) {
                checkbox.classList.remove("picked");
                pickedImages.splice(pickedImages.indexOf(image.id));
                list.value = pickedImages;
            } else {
                console.error("Element provided is null or undefined.");
            }
            console.log(pickedImages);
        }
    }

    document.addEventListener('DOMContentLoaded', (event) => {
        const customMenu = document.getElementById("custom-menu");
        const images = document.querySelectorAll('.ad_img');

        images.forEach(image => {
            image.addEventListener('contextmenu', function(e) {
                e.preventDefault();

                customMenu.style.top = `${e.pageY}px`;
                customMenu.style.left = `${e.pageX}px`;
                customMenu.style.display = 'block';
            });
        });

        document.addEventListener("click", () => {
            customMenu.style.display = "none";
        });

    });

    function handleOption1() {
        alert("Option 1 clicked!");
    }

    function handleOption2() {
        alert("Option 2 clicked!");
    }


    function showEditForm() {
        let form = document.getElementById("edit_form");
        form.style.display = "block";
        document.getElementById("showEditFormButton").style.display = "none";
        document.getElementById("hideEditFormButton").style.display = "block";
    }

    function hideEditForm() {
        let form = document.getElementById("edit_form");
        form.style.display = "none";
        document.getElementById("showEditFormButton").style.display = "block";
        document.getElementById("hideEditFormButton").style.display = "none";
    }

    function swap(diff, div) {
        var container = document.getElementById("order_section_list");

        if (diff > 0) {
            container.insertBefore(div.parentNode.nextElementSibling, div.parentNode);
        } else {
            container.insertBefore(div.parentNode, div.parentNode.previousElementSibling);
        }
    }

    function toggleOrderEditMode() {
    isOrderEditModeActive = !isOrderEditModeActive;
    if (isOrderEditModeActive) {
        const elems = document.getElementsByClassName("orderButtonUp");

        for (let i = 0; i < elems.length; i++) {
            elems[i].style.display = 'block'
        }

        const elemsMore = document.getElementsByClassName("orderButtonDown");

        for (let i = 0; i < elemsMore.length; i++) {
            elemsMore[i].style.display = 'block'
        }
        document.getElementById("editOrderButton").style.display = "none";
        document.getElementById("editOrderButtonApply").style.display = "block";
        document.getElementById("editOrderButtonCancel").style.display = "block";
    } else {
        const elems = document.getElementsByClassName("orderButtonUp");

        for (let i = 0; i < elems.length; i++) {
            elems[i].style.display = 'none'
        }

        const elemsMore = document.getElementsByClassName("orderButtonDown");

        for (let i = 0; i < elemsMore.length; i++) {
            elemsMore[i].style.display = 'none'
        }
        document.getElementById("editOrderButton").style.display = "block";
        document.getElementById("editOrderButtonApply").style.display = "none";
        document.getElementById("editOrderButtonCancel").style.display = "none";
    }
}

    function applyOrder() {
        const elems = document.getElementsByClassName("order");
        let list = document.getElementById("newOrder");

        for (let i = 0; i < elems.length; i++) {
            imagesOrder.push(elems[i].id);
        }
        list.value = imagesOrder;
        console.log(imagesOrder);
    }
