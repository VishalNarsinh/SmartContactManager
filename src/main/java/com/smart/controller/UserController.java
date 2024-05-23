package com.smart.controller;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smart.dao.ContactRepository;
import com.smart.dao.MyOrderRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.MyOrder;
import com.smart.entities.User;
import com.smart.helper.FileUploadHelper;
import com.smart.helper.Message;

import ch.qos.logback.core.net.server.Client;

import com.smart.helper.FileUploadHelper.FileUploadResult;

import com.razorpay.*;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	FileUploadHelper fileUploadHelper;

	@Autowired
	ContactRepository contactRepository;
	
	@Autowired
	MyOrderRepository orderRepository;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	// method for adding common data to response
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		User user = userRepository.getUserByUserName(principal.getName());
		model.addAttribute("user", user);
	}

	// dashboard - home
	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {
		model.addAttribute("title", "User Dashboard");
		return "normal/user_dashboard";
	}

	// open add form handler
	@GetMapping("/add-contact")
	public String addContactForm(Model model) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		model.addAttribute("heading", "Add new Contact");
		model.addAttribute("submitBtnValue", "Add Contact");
		model.addAttribute("action", "/user/process-contact");
		return "normal/add_contact_form";
	}

	// open update form handler
	@GetMapping("/update-contact/{cid}")
	public String updateContactForm(@PathVariable("cid") int cID, Model model, Principal principal,
			RedirectAttributes redirectAttributes) {
		User user = userRepository.getUserByUserName(principal.getName());
		Contact contact = contactRepository.findById(cID).get();
		if (user.getId() == contact.getUser().getId()) {
			model.addAttribute("title", "Edit Contact");
			model.addAttribute("contact", contact);
			model.addAttribute("heading", "Edit Contact Details");
			model.addAttribute("submitBtnValue", "Save Changes");
			model.addAttribute("action", "/user/update-contact");
			return "normal/add_contact_form";
		}
		redirectAttributes.addFlashAttribute("message", new Message("You can't update other's Contact", "danger"));
		return "redirect:/user/index";

	}

	@PostMapping("/process-contact")
	public String saveContact(@ModelAttribute Contact contact, @RequestParam("imagefile") MultipartFile imageFile,
			Principal principal, RedirectAttributes redirectAttributes) {
		try {
			User user = userRepository.getUserByUserName(principal.getName());
			processImage(imageFile, contact);
			contact.setUser(user);
			user.getContacts().add(contact);
			userRepository.save(user);
			Message message = new Message();
			message.setContent("Contact saved successfully");
			message.setType("success");
//			add as flash data
			redirectAttributes.addFlashAttribute("message", message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/user/add-contact";

	}

	private void processImage(MultipartFile imageFile, Contact contact) {

		try {
			if (!imageFile.isEmpty() && imageFile.getContentType().startsWith("image")) {
				FileUploadResult uploadFile = fileUploadHelper.uploadFile(imageFile);
				if (uploadFile.isSuccess()) {
					contact.setImage(uploadFile.getUniqueFileName());
				}
			} else {
				contact.setImage("default.png");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	show contact handler
//	per page = 5 {n}
//	current page=0 {page}
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") int page, Model model, Principal principal) {
		model.addAttribute("title", "Show Contacts");
		User user = userRepository.getUserByUserName(principal.getName());
//		current page
//		per page
		PageRequest pageRequest = PageRequest.of(page, 2);
		Page<Contact> contacts = contactRepository.findContactsByUserId(user.getId(), pageRequest);
		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());
		return "normal/show_contacts";

	}

//	showing specific contact detail
	@GetMapping("/contact/{cid}")
	public String showContactDetail(@PathVariable("cid") int cId, Model model, Principal principal,
			RedirectAttributes redirectAttributes) {
		Optional<Contact> contactOptional = contactRepository.findById(cId);
		Contact contact = contactOptional.get();
		User user = userRepository.getUserByUserName(principal.getName());
		if (user.getContacts().contains(contact)) {
			model.addAttribute("title", contact.getName());
			model.addAttribute("contact", contact);

		} else {
//			Not authorized(This contact does not belong to you)
			redirectAttributes.addFlashAttribute("message", new Message("You can't access other's Contact", "danger"));
			return "redirect:/user/index";
		}
//		if(user.getId()==contact.getUser().getId()) {
//			
//		}else {
////			Not authorized(This contact does not belong to you)			
//		}

		return "normal/contact_detail";
	}

	@GetMapping("/contact/delete/{cid}")
	public String deleteContact(@PathVariable("cid") int cId, Principal principal,
			RedirectAttributes redirectAttributes) {
//		contactRepository.deleteById(cId);
//		if you directly use above method anybody can delete through url
		Contact contact = contactRepository.findById(cId).get();
		User user = userRepository.getUserByUserName(principal.getName());
		if (user.getId() == contact.getUser().getId()) {
//			contact.setUser(null);
//			@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "user")
//			we used it in User entity for contact list
//			so it might not delete contact till it is linked with user
			fileUploadHelper.deleteFile(contact.getImage());
			contactRepository.delete(contact);
			redirectAttributes.addFlashAttribute("message", new Message("Contact Deleted", "success"));
		} else {
			redirectAttributes.addFlashAttribute("message", new Message("You can't delete other's Contact", "danger"));
		}

		return "redirect:/user/show-contacts/0";
	}

	@PostMapping("/update-contact")
	public String updateContact(@ModelAttribute Contact contact, @RequestParam("imagefile") MultipartFile multipartFile,
			RedirectAttributes redirectAttributes, Model model, Principal principal) {
		try {
			contact.setUser(userRepository.getUserByUserName(principal.getName()));
			Contact oldContact = contactRepository.findById(contact.getcId()).get();

			if (multipartFile.isEmpty()) {
				contact.setImage(oldContact.getImage());
			} else {
				processImage(multipartFile, contact);
				fileUploadHelper.deleteFile(oldContact.getImage());
			}
			Contact save = contactRepository.save(contact);
			model.addAttribute("title", "Edit Contact");
			model.addAttribute("contact", save);
			model.addAttribute("action", "/user/update-contact");
			redirectAttributes.addFlashAttribute("message", new Message("Contact Updated Successfully", "success"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/user/update-contact/" + contact.getcId();
	}

//	show user profile
	@GetMapping("/profile")
	public String userProfile(Model model) {
		model.addAttribute("title", "Profile Page");
		return "normal/profile";
	}

	@GetMapping("/edit")
	public String showEditPage(Model model) {
		model.addAttribute("title", "Edit Details");
		return "normal/user_edit";
	}

	@PostMapping("/update")
	public String updateContact(@ModelAttribute User newUser, @RequestParam("imagefile") MultipartFile multipartFile,
			Principal principal, RedirectAttributes redirectAttributes, Model model) {
		User oldUser = userRepository.getUserByUserName(principal.getName());
		newUser.setEnabled(oldUser.isEnabled());
		newUser.setPassword(oldUser.getPassword());
		newUser.setRole(oldUser.getRole());
		if (multipartFile.isEmpty()) {
			newUser.setImageUrl(oldUser.getImageUrl());
		} else {
			FileUploadResult uploadFile = fileUploadHelper.uploadFile(multipartFile);
			if (uploadFile.isSuccess()) {
				fileUploadHelper.deleteFile(oldUser.getImageUrl());
				newUser.setImageUrl(uploadFile.getUniqueFileName());
			}

		}
		User save = userRepository.save(newUser);
		model.addAttribute("user", save);
		redirectAttributes.addFlashAttribute("message", new Message("User Updated Successfully", "success"));
		return "redirect:/user/profile";
	}

//	open setting handler
	@GetMapping("/settings")
	public String openSettings(Model model) {
		model.addAttribute("title", "Settings");
		return "normal/settings";
	}

	@PostMapping("/change-password")
	public String changePassword(@RequestParam("old_password") String oldPassword,
			@RequestParam("new_password") String newPassword, Principal principal,
			RedirectAttributes redirectAttributes) {

		User user = userRepository.getUserByUserName(principal.getName());
		if(bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
			user.setPassword(bCryptPasswordEncoder.encode(newPassword));
			userRepository.save(user);
			redirectAttributes.addFlashAttribute("message",new Message("Password changed Successfully","success"));
			return "redirect:/user/profile";
		}else {
			redirectAttributes.addFlashAttribute("message",new Message("Incorrect Old Password","danger"));
			return "redirect:/user/settings";
		}

		
	}
	
	

	@PostMapping("/create_order")
	@ResponseBody
	public String order(@RequestBody Map<String, Object> data,Principal principal) {
		System.out.println(data);
		int amount = Integer.parseInt(data.get("amount").toString());
		Order order = null;
		try {
			var client = new RazorpayClient("rzp_test_u8dWKOGCtW7j9P","DJi5qTBddv0uyNbspy8Guu26");
			JSONObject object = new JSONObject();
			object.put("amount", amount*100);
			object.put("currency", "INR");
//we used india currency that's why we have to pass amount in form of paisa
//we have to pass amount in paisa that's why multiplied it with 100
			object.put("receipt", "txn_123456");
//			creating new order
			order = client.orders.create(object);
			
			MyOrder myOrder = new MyOrder(order,userRepository.getUserByUserName(principal.getName()));
			orderRepository.save(myOrder);
			System.out.println(order);
		} catch (RazorpayException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return order.toString();
	}
	
	

	@PostMapping("/update_order")
	public ResponseEntity<?> postMethodName(@RequestBody Map<String, Object> data) {
		MyOrder order = orderRepository.findByOrderId(data.get("order_id").toString());
		order.setPaymentId(data.get("payment_id").toString());
		order.setStatus(data.get("status").toString());
		orderRepository.save(order);
		return ResponseEntity.ok(Map.of("message","successful"));
	}


}
