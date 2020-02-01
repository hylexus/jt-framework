package io.github.hylexus.jt808.samples.annotation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hylexus
 * Created At 2020-02-01 7:01 下午
 */
@RestController
public class TestController {

    @GetMapping("/test")
    public Map<String, Object> test(@RequestParam("a") String a, @RequestParam("list") List<String> list) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("a", a);
        map.put("list", list);
        return map;
    }
}
