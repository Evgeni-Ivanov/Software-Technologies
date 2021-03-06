package shoppinglist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import shoppinglist.bindingModel.ProductBindingModel;
import shoppinglist.entity.Product;
import shoppinglist.repository.ProductRepository;

import java.util.List;

@Controller
public class ProductController {

	private final ProductRepository productRepository;

	@Autowired
	public ProductController(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@GetMapping("/")
	public String index(Model model) {
		List<Product> products = productRepository.findAll();

		model.addAttribute("products", products);
		model.addAttribute("view", "product/index");

		return "base-layout";
	}

	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("view", "product/create");

		return "base-layout";
	}

	@PostMapping("/create")
	public String createProcess(Model model, ProductBindingModel productBindingModel) {
		if (productBindingModel == null) {
			return "redirect:/";
		}

		if (productBindingModel.getPriority() < 0 || productBindingModel.getName().equals("")
				|| productBindingModel.getQuantity() < 0 || productBindingModel.getStatus().equals("")){
			return "redirect:/";
		}

		Product product = new Product(productBindingModel.getPriority(), productBindingModel.getName(),
				productBindingModel.getQuantity(), productBindingModel.getStatus());

		productRepository.saveAndFlush(product);

		return "redirect:/";
	}

	@GetMapping("/edit/{id}")
	public String edit(Model model, @PathVariable int id) {
		Product product = productRepository.findOne(id);

		if (product != null) {
			model.addAttribute("product", product);
			model.addAttribute("view", "product/edit");

			return "base-layout";
		}

		return "redirect:/";
	}

	@PostMapping("/edit/{id}")
	public String editProcess(Model model, @PathVariable int id, ProductBindingModel productBindingModel, Product productModel) {
		if (productModel.getPriority() < 0 || productModel.getName().equals("") || productModel.getQuantity() < 0 || productModel.getStatus().equals("")) {
			productModel.setId(id);
			model.addAttribute("product", productModel);
			model.addAttribute("view", "product/edit");

			return "base-layout";
		}

		Product product = productRepository.findOne(id);

		if (product != null) {
			product.setPriority(productModel.getPriority());
			product.setName(productModel.getName());
			product.setQuantity(productModel.getQuantity());
			product.setStatus(productModel.getStatus());
			productRepository.saveAndFlush(product);
		}

		return "redirect:/";
	}
}
