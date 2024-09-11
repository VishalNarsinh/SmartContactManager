document.addEventListener("DOMContentLoaded", function () {
  // document
  //   .querySelector("#upload_image_preview")
  //   .addEventListener("load", function () {
  //     console.log("image preview loaded");
  //     if (!this.hasAttribute("src") || this.src === "") {
  //       this.style.display = "none";
  //       console.log("Src is empty");
  //     } else {
  //       this.style.display = "block";
  //       console.log("Src is not empty");
  //     }
  //   });

  document
    .querySelector("#add_contact_button")
    .addEventListener("click", function () {
      this.style.display = "none";
      document.querySelector("#spinner").style.display = "block";
    });

  document
    .querySelector("#contactImage")
    .addEventListener("change", function (event) {
      const file = event.target.files[0];

      if (!file || !file.type.startsWith("image/")) {
        Swal.fire({
          title: "Please select an Image file type",
          text: "{jpeg,jpg,gif,png,webp}",
          icon: "warning",
          showCancelButton: true,
          cancelButtonColor: "#d33",
        });
        event.target.value = "";
        document.querySelector("#upload_image_preview").style.display = "none";
        return;
      }

      const fileReader = new FileReader();
      fileReader.onload = function () {
        document.querySelector("#upload_image_preview").src = fileReader.result;
        if (file)
          document.querySelector("#upload_image_preview").style.display =
            "block";
      };
      if (file) fileReader.readAsDataURL(file);
    });
});
