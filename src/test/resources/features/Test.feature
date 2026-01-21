Feature: Automatizacion de reprogramacion de cita para Visado

  @001
  Scenario: Usuario reprograma cita de visado de forma exitosa
    Given El usuario está en la página de Visado
    When luego se loguea de forma exitosa
    Given inicia la reprogramacion de la cita
    Then confirma la reprogramacion
