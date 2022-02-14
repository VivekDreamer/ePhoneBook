package com.app.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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

import com.app.dao.ContactRepository;
import com.app.dao.OrderRepository;
import com.app.dao.UserRepository;
import com.app.entities.Contact;
import com.app.entities.OrdersDone;
import com.app.entities.User;
import com.app.helper.Message;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;


@Controller
@RequestMapping("/user")
public class ControllerUser {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private OrderRepository orderRepository;

	
	// method for adding common data to response
	@ModelAttribute
	public void addCommonData(Model m, Principal p) {
		String name = p.getName();
		System.out.println("name : " + name);
		User user = userRepository.getUserByUserName(name);
		System.out.println("user:-------->" + user);
		m.addAttribute("user", user);
	}

	// home dashboard
	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {

		return "Normal/userHome";
	}

	
	// add form handler
	@GetMapping("/addContact")
	public String addContactForm(Model model) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		return "Normal/addContactForm";
	}

	// processing add-contact form
	@PostMapping("/process_contact")
	public String processContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Principal principal, HttpSession session) {
		try {
			
			  System.out.println("data--------------------------------------");
			  System.out.println(contact);
			  
			  
			  System.out.println("*******************************************");
			  System.out.println("contact id : "+contact.getCid());
			  System.out.println("contact name "+contact.getName());
			  System.out.println("contact email "+contact.getEmail());
			  System.out.println("contact phone "+contact.getPhone());
			 System.out.println("**********************************************");
			 Random rand = new Random();
			 int randomNumber = rand.nextInt(100000000);
			String name = principal.getName();
			User user = this.userRepository.getUserByUserName(name);

			// processing and uploading file
			if (file.isEmpty()) {
				// if file is empty
				contact.setImage("contact.png");
			} else {
				// uploading file to folder and update the name in contact
				contact.setImage(randomNumber + file.getOriginalFilename());
				File savedImage = new ClassPathResource("static/CONTACT_PROFILE").getFile();
				Path path = Paths.get(
						savedImage.getAbsolutePath() + File.separator + randomNumber + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("image is uploaded!!!");
			}
			contact.setUser(user);
			user.getContacts().add(contact);
			this.userRepository.save(user);
			System.out.println("added in database");

			// success message
			session.setAttribute("message", new Message("Contact saved successfully!!! Add new:)", "success"));

		} catch (Exception e) {
			System.out.println("ERROR------>" + e.getMessage());
			e.printStackTrace();
			// error message
			session.setAttribute("message", new Message("Something went wrong!!! Try again:(", "danger"));
		}
		return "Normal/addContactForm";
	}

	// view contacts handler
	// per page n =4 contacts
	// current page = 0 (page)
	@GetMapping("/show_contacts/{page}")
	public String viewContacts(@PathVariable("page") Integer page, Model m, Principal p) {

		m.addAttribute("title", "show user contacts");
		// sending contact list of signed in user
		/*
		 * String userName = p.getName(); User user =
		 * this.userRepository.getUserByUserName(userName); List<Contact> contacts =
		 * user.getContacts();
		 */
		String userName = p.getName();
		User user = this.userRepository.getUserByUserName(userName);

		Pageable pageable = PageRequest.of(page, 4);
		Page<Contact> contacts = this.contactRepository.findContactByUser(user.getId(), pageable);
		m.addAttribute("contacts", contacts);
		m.addAttribute("currentPage", page);
		m.addAttribute("totalPages", contacts.getTotalPages());
		m.addAttribute("numOfPages", contacts.getNumberOfElements());
		return "Normal/show_contacts";
	}

	//showing particular contacts
	@RequestMapping("/contact/Cont{cid}")
	public String showParticularContact(@PathVariable("cid") int cid, Model model, Principal principal) {

		Optional<Contact> contactOptional = this.contactRepository.findById(cid);
		Contact contact = contactOptional.get();
		System.out.println(cid);
		String userName = principal.getName();
		
		User user = this.userRepository.getUserByUserName(userName);
		
		if(user.getId()==contact.getUser().getId())
		{
			model.addAttribute("contact", contact);
			model.addAttribute("title", contact.getName());
		}
		return "Normal/contact_detail";
	}
	
	//showing user profile
	@GetMapping("/profile")
	public String showLoginUserProfile(Model model) {
		model.addAttribute("title", "Profile Page");
		return "Normal/showUserProfile";
		
	}
	
	//DELETE CONTACT HANDLER
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cid,Model model,Principal principal,HttpSession session) {
		try {
		Optional<Contact> id = this.contactRepository.findById(cid);
		Contact contact = id.get();
		String name = principal.getName();
		User user = this.userRepository.getUserByUserName(name);
		if(user.getId()==contact.getUser().getId()) {
			if(!(contact.getImage().equals("contact.png"))) {
				System.out.println("hii in delete");
				File f= new ClassPathResource("/static/CONTACT_PROFILE"+File.separator+contact.getImage()).getFile(); 
				f.delete();
			}
			//deleting contact
			user.getContacts().remove(contact);
			this.userRepository.save(user);
			
			System.out.println("deleted successfully");
			session.setAttribute("message", new Message("Contact deleted successfully!!!","success"));
		}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/user/show_contacts/0";
	}
	
	//OPENING UPDATE CONTACT HANDLER
	@PostMapping("/update/{cid}")
	public String updateForm(Model model, @PathVariable("cid") Integer cid) {
		model.addAttribute("title", "UPDATE");
		Contact contact = this.contactRepository.findById(cid).get();
		model.addAttribute("contact", contact);
		return "Normal/updateForm";
	}
	
	//UPDATE CONTACT HANDLER
	@PostMapping("/process_updateContact")
	public String processUpdateHandler(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file, HttpSession session, Model model,Principal pricipal) {
		
		Contact oldContactDetails = this.contactRepository.findById(contact.getCid()).get();
		Random rand = new Random();
		 int randomNum = rand.nextInt(100000000);
		try {
			if(!file.isEmpty()) {
				
				//delete old photo from target folder
				File deletedImage = new ClassPathResource("static/CONTACT_PROFILE").getFile();
				File file1 = new File(deletedImage, oldContactDetails.getImage());
				file1.delete();
				
				
				//image update
				contact.setImage(randomNum + file.getOriginalFilename());
				File savedImage = new ClassPathResource("static/CONTACT_PROFILE").getFile();
				Path path = Paths.get(
						savedImage.getAbsolutePath() + File.separator + randomNum + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}
			else {
				//if file empty then, set old image
				contact.setImage(oldContactDetails.getImage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		User user = this.userRepository.getUserByUserName(pricipal.getName());
		contact.setUser(user);
		this.contactRepository.save(contact);
		session.setAttribute("message", new Message("successfully updated.","success"));
		return "redirect:/user/contact/Cont"+contact.getCid();           
	}
	
	//updating user profile who adds contacts
	@PostMapping("/update/profile")
	public String updateUserForm(Model model, Principal principal) {
		model.addAttribute("title", "UPDATE PROFILE");
		String name = principal.getName();
		User user = this.userRepository.getUserByUserName(name);
		model.addAttribute("user", user);
		return "Normal/update-profile";
	}
	
	//updating user profile processing
	@PostMapping("/process_updateUser")
	public String processUserUpdateHandler(@ModelAttribute User user, @RequestParam("userProfileImage") MultipartFile file, HttpSession session, Model model, Principal principal) {
//		System.out.println("************************************************************************8");
//		System.out.println(user);
		String name = principal.getName();
		User oldUser = this.userRepository.getUserByUserName(name);
		Random rand = new Random();
		int randomNum = rand.nextInt(100000000);
		
		try {
			if(!file.isEmpty()) {
				//if file is not empty, then, delete old image from target folder
				File deletedImage = new ClassPathResource("static/USER_PROFILE").getFile();
				File file1 = new File(deletedImage, oldUser.getImageUrl());
				file1.delete();
				
				//now, update the image
				user.setImageUrl(randomNum + file.getOriginalFilename());
				File savedImage = new ClassPathResource("static/USER_PROFILE").getFile();
				Path path = Paths.get(
						savedImage.getAbsolutePath() + File.separator + randomNum + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}
			else {
				//if file is empty then, set old image
				user.setImageUrl(oldUser.getImageUrl());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		user.setContacts(oldUser.getContacts());
		this.userRepository.save(user);
		session.setAttribute("message", new Message("successfully updated.","success"));
		return "redirect:/user/profile";
	}
	
	@PostMapping("/delete")
	public String deleteUser(Principal principal, Model model) {
		
		String name = principal.getName();
		User user = this.userRepository.getUserByUserName(name);
		List<Contact> contacts = user.getContacts();
		int i = 0;
		
		if(contacts.size()!=0) {
		try {
			//deleting all contacts associated with user along with their image saved in target folder
			while( i < contacts.size()) {		
				if(!(this.contactRepository.getById(contacts.get(i).getCid()).getImage().equals("contact.png"))) {
					System.out.println("hii in delete");
					File f= new ClassPathResource("/static/CONTACT_PROFILE"+File.separator+this.contactRepository.getById(contacts.get(i).getCid()).getImage()).getFile(); 
					f.delete();
				}
				//deleting contact
				contacts.remove(this.contactRepository.getById(contacts.get(i).getCid()));
			}
	      //*************************************************************************************************************************************************************
			this.userRepository.save(user);
		}
		 catch (Exception e) {
			e.printStackTrace();
		}
	}
		try {
			//deleting user image from target folder
			if(!user.getImageUrl().equals("userImage.jpg")) {
				File f= new ClassPathResource("/static/USER_PROFILE"+File.separator+user.getImageUrl()).getFile(); 
				f.delete();
			}
			//deleting user
			this.userRepository.deleteById(user.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/signin";
	}
	
	//creating order for payment
	@PostMapping("/createOrder")
	@ResponseBody
	public String createOrder(@RequestBody Map<String , Object> data, Principal principal) throws Exception {
//		System.out.println("hey order function");
//		System.out.println(data);
		
		
		int amount = Integer.parseInt(data.get("amount").toString());
		RazorpayClient razorpayClient = new RazorpayClient("rzp_test_ckUbwQFcrzCweq", "d9mZeFaSPpv7tIXsq9F7iFNo");
		JSONObject ob = new JSONObject();
		
		//generating 6 digit OTP
		int min = 100000;
		int max = 999999;
		int receiptNum = (int)(Math.random()*(max-min+1)+min);  
		ob.put("amount", amount*100);
		ob.put("currency", "INR");
		ob.put("receipt", "tx"+receiptNum);
		
		Order order = razorpayClient.Orders.create(ob);
		System.out.println("*************************************************************************************");
		System.out.println(order);
		
		
		Date date = order.get("created_at");
		System.out.println("______________________________________________________________________________________");
		System.out.println(date);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
		String strDate= formatter.format(date);
		System.out.println(strDate);
		int amountpaid = (int)order.get("amount")/100;
	
		//save the order in your database to keep record
		OrdersDone orders = new OrdersDone();
		orders.setAmount(amountpaid+"");
		orders.setOrderId(order.get("id"));
		orders.setRecieptNum(order.get("receipt"));
		orders.setPaymentId(null);
		orders.setTime(strDate);
		orders.setStatus("Created");
		orders.setUser(this.userRepository.getUserByUserName(principal.getName()));		
		
		this.orderRepository.save(orders);
		return order.toString();
		
	}
	
	//updating order after payment done
	@PostMapping("/updateOrder")
	public ResponseEntity<?> updateOrder(@RequestBody Map<String, Object> data){
		OrdersDone ordersDone = this.orderRepository.findByOrderId(data.get("order_id").toString());
		ordersDone.setPaymentId(data.get("payment_id").toString());
		ordersDone.setStatus(data.get("status").toString());
		this.orderRepository.save(ordersDone);
		System.out.println(data);
		
		return ResponseEntity.ok(Map.of("msg","updated"));
	}
}
