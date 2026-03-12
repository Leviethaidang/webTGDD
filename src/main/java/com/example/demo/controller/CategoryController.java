@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("category", new Category());
        return "category-form";
    }

    @PostMapping("/add")
    public String addCategory(@Valid Category category, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "category-form"; // form sẽ hiển thị lỗi validation
        }

        try {
            categoryRepository.save(category);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Không thể lưu danh mục: " + e.getMessage());
            return "category-form";
        }

        return "redirect:/categories/list"; // redirect về danh sách category
    }

    @GetMapping("/list")
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "category-list"; // view hiển thị tất cả category
    }
}