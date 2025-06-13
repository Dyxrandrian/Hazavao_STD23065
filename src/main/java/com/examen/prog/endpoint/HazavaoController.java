package com.examen.prog.endpoint;

import com.examen.prog.services.ChatGptService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hazavao")
public class HazavaoController {

  private final ChatGptService chatGptService;

  public HazavaoController(ChatGptService chatGptService) {
    this.chatGptService = chatGptService;
  }

  @GetMapping
  public String getDefinition(@RequestParam String teny) {
    return chatGptService.getDefinitionInMalagasy(teny);
  }
}
