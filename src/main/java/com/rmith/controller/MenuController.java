package com.rmith.controller;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.rmith.config.AsyncThreadConfig;
import com.rmith.constant.Constant;
import com.rmith.dto.CategoryDTO;
import com.rmith.dto.ModuleDTO;
import com.rmith.service.CategoryService;
import com.rmith.service.MenuService;
import com.rmith.service.ModuleService;
import java.util.concurrent.CompletableFuture;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//</editor-fold>

/**
 *
 * @author Le Tan Phuc
 
 */
@Controller
public class MenuController {

    @Autowired
    @Lazy
    private CategoryService categoryService;

    @Autowired
    @Lazy
    private MenuService menuService;

    @Autowired
    @Lazy
    private ModuleService moduleService;

    private final Logger logger = LogManager.getLogger("error_future");

    /* 1: Categories */
    //<editor-fold defaultstate="collapsed" desc="CATEGORY PAGE">
    @GetMapping("/categories")
    public DeferredResult<String> getCategory(
            HttpSession session,
            ModelMap map) {
        map.addAttribute("menuModule", "categories");
        DeferredResult<String> output = new DeferredResult(30000L);

        CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> {
                    map.addAttribute("menuList", menuService.getMenuList());
                }, AsyncThreadConfig.executorPool),
                CompletableFuture.runAsync(() -> {
                    map.addAttribute("categoryList", categoryService.getAllCategoryList());
                }, AsyncThreadConfig.executorPool)
        ).exceptionally(t -> {
            logger.error(ExceptionUtils.getStackTrace(t));
            output.setResult("/menu/categories");
            return null;
        }).thenApply(t -> {
            output.setResult("/menu/categories");
            return null;
        });
        return output;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ADD NEW CATEGORY">
    @PostMapping("/add-new-category")
    public String addNewCategory(
            HttpSession session,
            CategoryDTO categoryDTO,
            RedirectAttributes redirectAttributes) {

        if (categoryDTO.getErrorMap().size() > 0) {
            redirectAttributes.addFlashAttribute("categoryDTO", categoryDTO);
            redirectAttributes.addFlashAttribute("myStatus", "add");
            return "redirect:/categories";
        }
        categoryDTO.setCreatedBy((int) session.getAttribute("accountID"));
        String result = categoryService.addCategory(categoryDTO);
        switch (result) {
            case Constant.SUCCESS:
                redirectAttributes.addFlashAttribute("addSuccess", true);
                break;
            default:
                redirectAttributes.addFlashAttribute("addError", true);
                break;
        }
        return "redirect:/categories";
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="UPDATE CATEGORY">
    @PostMapping(value = "/update-category")
    public String updateCategory(
            HttpSession session,
            CategoryDTO categoryDTO,
            RedirectAttributes redirectAttributes) {
        if (categoryDTO.getErrorMap().size() > 0) {
            redirectAttributes.addFlashAttribute("categoryDTO", categoryDTO);
            redirectAttributes.addFlashAttribute("myStatus", "edit");
            return "redirect:/categories";
        }
        categoryDTO.setUpdatedBy((int) session.getAttribute("accountID"));
        String result = categoryService.updateCategory(categoryDTO);
        switch (result) {
            case Constant.SUCCESS:
                redirectAttributes.addFlashAttribute("updateSuccess", true);
                break;
            default:
                redirectAttributes.addFlashAttribute("updateError", true);
                break;
        }
        return "redirect:/categories";
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="DELETE CATEGORY">
    @PostMapping("/delete-category")
    public String deleteCategory(
            @RequestParam int categoryId,
            RedirectAttributes redirectAttributes) {
        String result = categoryService.deleteCategory(categoryId);
        switch (result) {
            case Constant.SUCCESS:
                redirectAttributes.addFlashAttribute("deleteSuccess", true);
                break;
            case Constant.HAS_REF:
                redirectAttributes.addFlashAttribute("categoryHasModule", true);
                break;
            default:
                redirectAttributes.addFlashAttribute("deleteError", true);
                break;
        }
        return "redirect:/categories";
    }
    //</editor-fold>

    /* 2: Modules */
    //<editor-fold defaultstate="collapsed" desc="MODULE PAGE">
    @GetMapping("/modules")
    public DeferredResult<String> getModules(
            HttpSession session,
            ModelMap map) {
        map.addAttribute("menuModule", "modules");
        DeferredResult<String> output = new DeferredResult(30000L);

        CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> {
                    map.addAttribute("moduleList", moduleService.getAllModule());
                }, AsyncThreadConfig.executorPool),
                CompletableFuture.runAsync(() -> {
                    map.addAttribute("menuList", menuService.getMenuList());
                }, AsyncThreadConfig.executorPool),
                CompletableFuture.runAsync(() -> {
                    map.addAttribute("categoryList", categoryService.getAllCategory());
                }, AsyncThreadConfig.executorPool)
        ).exceptionally(t -> {
            logger.error(ExceptionUtils.getStackTrace(t));
            output.setResult("/menu/modules");
            return null;
        }).thenApply(t -> {
            output.setResult("/menu/modules");
            return null;
        });
        return output;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ADD NEW MODULE">
    @PostMapping("/add-new-module")
    public String addNewModule(
            HttpSession session,
            ModuleDTO moduleDTO, 
            RedirectAttributes redirectAttributes) {

        if (moduleDTO.getErrorMap().size() > 0) {
            redirectAttributes.addFlashAttribute("moduleDTO", moduleDTO);
            redirectAttributes.addFlashAttribute("myStatus", "add");
            return "redirect:/modules";
        }
        moduleDTO.setCreatedBy((int) session.getAttribute("accountID"));
        String result = moduleService.addModule(moduleDTO);
        switch (result) {
            case Constant.SUCCESS:
                redirectAttributes.addFlashAttribute("addSuccess", true);
                break;
            default:
                redirectAttributes.addFlashAttribute("addError", true);
                break;
        }
        return "redirect:/modules";
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="UPDATE MODULE">
    @PostMapping(value = "/update-module")
    public String updateModule(
            HttpSession session,
            ModuleDTO moduleDTO,
            RedirectAttributes redirectAttributes) {
        if (moduleDTO.getErrorMap().size() > 0) {
            redirectAttributes.addFlashAttribute("moduleDTO", moduleDTO);
            redirectAttributes.addFlashAttribute("myStatus", "edit");
            return "redirect:/modules";
        }
        moduleDTO.setUpdatedBy((int) session.getAttribute("accountID"));
        String result = moduleService.updateModule(moduleDTO);
        switch (result) {
            case Constant.SUCCESS:
                redirectAttributes.addFlashAttribute("updateSuccess", true);
                break;
            default:
                redirectAttributes.addFlashAttribute("updateError", true);
                break;
        }
        return "redirect:/modules";
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="DELETE MODULE">
    @PostMapping("/delete-module")
    public String deleteModule(
            @RequestParam int moduleId,
            RedirectAttributes redirectAttributes) {
        String result = moduleService.deleteModule(moduleId);
        switch (result) {
            case Constant.SUCCESS:
                redirectAttributes.addFlashAttribute("deleteSuccess", true);
                break;
            default:
                redirectAttributes.addFlashAttribute("deleteError", true);
                break;
        }
        return "redirect:/modules";
    }
    //</editor-fold>

}
