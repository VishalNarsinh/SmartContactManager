const base_url = "http://localhost:9090";
const target = document.getElementById("view_contact_modal");

const options = {
  placement: "bottom-right",
  backdrop: "dynamic",
  backdropClasses: "bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40",
  closable: true,
};

// instance options object
const instanceOptions = {
  id: "view_contact_modal",
  override: true,
};

const modal = new Modal(target, options, instanceOptions);

const openModal = () => {
  modal.show();
};

const closeModal = () => {
  modal.hide();
};

const loadContact = (contactId) => {
  fetch(`${base_url}/api/contact/${contactId}`)
    .then(async (response) => {
      const data = await response.json();
      document.querySelector("#contact_name").textContent = data.name;
      document.querySelector(
        "#contact_email"
      ).innerHTML = `<a class="text-blue-600 dark:text-blue-400 " href='mailto:${data.email}'>${data.email}</a>`;
      document.querySelector("#contact_phoneNumber").innerHTML =
        data.phoneNumber;
      document.querySelector("#contact_address").innerHTML = data.address;

      const image = document.createElement("img");
      image.style.maxHeight = "350px";
      image.style.maxWidth = "450px";

      image.src = data.picture;
      image.classList.add(
        ..."border-1 mx-auto rounded-lg shadow m-3 ring-2 ring-fuchsia-500".split(
          " "
        )
      );
      document.querySelector("#contact_image").innerHTML = "";
      document.querySelector("#contact_image").appendChild(image);

      document.querySelector("#contact_description").innerHTML =
        data.description;
      if (data.favourite) {
        document.querySelector(
          "#favorite"
        ).innerHTML = `<i class="fa-solid fa-star dark:text-blue-600"></i>`;
      }
      let links = "";
      if (data.websiteLink) {
        links += `<div id="websiteLink"> 
              <a href="${data.websiteLink}"  target="_blank">
                <i class="fa-solid fa-link text-xl me-3 text-blue-600"></i>
              </a>
            </div> `;
      }
      if (data.linkedinLink) {
        links += `<div id="linkedinLink">
          <a href="${data.linkedinLink}" target="_blank">
            <i class="fa-brands fa-linkedin text-xl text-blue-600"></i>
          </a>
        </div>`;
      }
      document.querySelector("#links").innerHTML = links;
      openModal();
      console.log(data);
    })
    .catch(async (err) => {
      console.log(err);
    });
};

const deleteContact = (contactId) => {
  Swal.fire({
    title: "Do you want to delete the contact?",
    text: "You won't be able to revert this!",
    icon: "warning",
    showCancelButton: true,
    confirmButtonColor: "#3085d6",
    cancelButtonColor: "#d33",
    confirmButtonText: "Delete!",
        backdrop: `
      rgba(0,0,123,0.4)
      url("https://media.tenor.com/-AyTtMgs2mMAAAAi/nyan-cat-nyan.gif")
      left top
      no-repeat
    `,
  }).then((result) => {
    if (result.isConfirmed) {
      let url = `${base_url}/user/contact/delete/${contactId}`;
      window.location.replace(url);
    }
  });
};
