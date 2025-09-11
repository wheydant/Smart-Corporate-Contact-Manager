console.log("Contact.js")

// Handle dropdown selection
document.querySelectorAll("#dropdown-search button").forEach((btn) => {
    btn.addEventListener("click", function () {
        const field = this.dataset.field;
        const icon = this.dataset.icon;
        const label = this.textContent.trim();

        document.getElementById("search-field").value = field;
        document.getElementById("search-label").textContent = label;
        document.getElementById("search-icon").className =
            "fa-solid " + icon + " h-4 w-4";

        document.getElementById("dropdown-search").classList.add("hidden");
    });
});

// Optional: Close dropdown on outside click
document.addEventListener("click", function (e) {
    if (!document.getElementById("dropdown-button").contains(e.target)) {
        document.getElementById("dropdown-search").classList.add("hidden");
    }
});

//-----Modal Configuration from Flowbite

// set the modal menu element
const $targetEl = document.getElementById('view-contact-modal');

// options with default values
const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
  id: 'view-contact-modal',
  override: true
};


// import { Modal } from 'flowbite';

/*
 * $targetEl: required
 * options: optional
 */
const contactModal = new Modal($targetEl, options, instanceOptions);

//------------------------

function openContactModal(){
    contactModal.show();
}

function closeContactModal(){
    contactModal.hide();
}

async function loadContactData(id, action = "view") {
  console.log("Loading contact:", id);

  try {
    const response = await fetch(`http://localhost:8081/api/contacts/${id}`);
    if (!response.ok) throw new Error("Failed to fetch contact");
    const data = await response.json();
    console.log("Fetched contact data:", data);
    // document.getElementById("contact-id").textContent = data.id;
    // Fill modal fields
    document.getElementById("contact-image").src = data.picture? data.picture : "/images/profile.png";

    document.getElementById("contact-name").textContent = data.name || "N/A";
    document.getElementById("contact-email").textContent = data.email || "N/A";
    document.getElementById("contact-phone").textContent = data.phoneNumber || "N/A";
    document.getElementById("contact-address").textContent = data.address || "N/A";
    document.getElementById("contact-description").textContent = data.description || "N/A";

    // Website link
    const websiteLink = document.getElementById("contact-website");
    if (data.websiteLink) {
      websiteLink.innerText = data.websiteLink;
      websiteLink.href = data.websiteLink;
      websiteLink.classList.remove("hidden");
    } else {
      websiteLink.classList.add("hidden");
    }

    // LinkedIn link
    const linkedInLink = document.getElementById("contact-linkedin");
    if (data.linkedInLink) {
      linkedInLink.innerText = data.linkedInLink;
      linkedInLink.href = data.linkedInLink;
      linkedInLink.classList.remove("hidden");
    } else {
      linkedInLink.classList.add("hidden");
    }

    // Favorite
    const favIcon = document.getElementById("contact-fav");
    favIcon.className = data.favorite
      ? "fa-solid fa-heart text-red-500"
      : "fa-regular fa-heart text-gray-400";

    document.getElementById("delete-contact-btn").href = `/user/contacts/delete/${data.id}/${data.name}`;

    document.getElementById("edit-contact-btn").href = `/user/contacts/edit-view/${data.id}`;


    // Delete button only for delete action
    document.getElementById("delete-contact-wrapper").style.display = action === "delete" ? "block" : "none";

    // Edit button only for edit action
    document.getElementById("edit-contact-wrapper").style.display = action === "edit" ? "block" : "none";

    openContactModal();
  } catch (error) {
    console.error("Error loading contact:", error);

    // Show fallback content
    document.getElementById("contact-name").textContent = "Contact not found";
    document.getElementById("contact-email").textContent = "";
    document.getElementById("contact-phone").textContent = "";
    document.getElementById("contact-address").textContent = "";
    document.getElementById("contact-description").textContent = "";
    document.getElementById("contact-image").src = "/images/profile.png";

    document.getElementById("contact-website").classList.add("hidden");
    document.getElementById("contact-linkedin").classList.add("hidden");
    document.getElementById("contact-fav").className =
      "fa-regular fa-heart text-gray-400";

    document.getElementById("delete-contact-btn").href = "#";

    openContactModal();
  }
}

// Looks Ugly
// function confirmDelete(anchor) {
//   return confirm("Are you sure you want to delete this contact?");
// }
// Handle form submit
/*document
  .getElementById("contact-search-form")
  .addEventListener("submit", function (e) {
    e.preventDefault();
    const field = document.getElementById("search-field").value;
    const query = document.getElementById("search-input").value;
    window.location.href = `/user/contacts/search?field=${field}&q=${encodeURIComponent(
      query
    )}`;
  });*/

// Add search functionality
/*document
  .getElementById("table-search-users")
  .addEventListener("input", function (e) {
    const searchTerm = e.target.value.toLowerCase();
    const tableRows = document.querySelectorAll("tbody tr");

    tableRows.forEach((row) => {
      const name = row
        .querySelector("th div:first-child")
        .textContent.toLowerCase();
      const email = row
        .querySelector("th div:last-child")
        .textContent.toLowerCase();

      if (name.includes(searchTerm) || email.includes(searchTerm)) {
        row.style.display = "";
      } else {
        row.style.display = "none";
      }
    });
  });*/