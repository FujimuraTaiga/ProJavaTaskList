package jp.gihyo.projava.tasklist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
public class HomeController {

    record TaskItem(String id, String task, String deadline, boolean done){}

    private final TaskListDao dao;

    @Autowired
    HomeController(TaskListDao dao){
        this.dao = dao;
    }

    @RequestMapping(value = "/hello")
    String hello(Model model){
        model.addAttribute("time",LocalDateTime.now());
        return "hello.html";
    }

    @RequestMapping(value = "/list")
    String list(Model model){
        List<TaskItem> taskItems = dao.findAll();
        model.addAttribute("taskList",taskItems);
        return "home.html";
    }

    @GetMapping(value = "/add")
    String add(@RequestParam String task, @RequestParam String deadline){
        String id = UUID.randomUUID().toString().substring(0,8);
        TaskItem item = new TaskItem(id,task,deadline,false);
        dao.add(item);
        return "redirect:/list";
    }

    @GetMapping(value = "/delete")
    String delete(@RequestParam String id){
        dao.delete(id);
        return "redirect:/list";
    }

    @GetMapping(value = "/update")
    String update(@RequestParam("id") String id,
                  @RequestParam("task") String task,
                  @RequestParam("deadline") String deadline,
                  @RequestParam("done") boolean done){
        TaskItem taskItem = new TaskItem(id,task,deadline,done);
        dao.update(taskItem);
        return "redirect:/list";
    }
}
