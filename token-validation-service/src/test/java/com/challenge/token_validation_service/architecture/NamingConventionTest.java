package com.challenge.token_validation_service.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RestController;

class NamingConventionTest {

  private static final String BASE_PACKAGE = "com.challenge.token_validation_service";

  private com.tngtech.archunit.core.domain.JavaClasses classes;

  @BeforeEach
  void setup() {
    classes = new ClassFileImporter().importPackages(BASE_PACKAGE);
  }

  @Test
  void classesInDomainServicesPackageShouldEndWithService() {

    classes()
        .that()
        .resideInAPackage("..domain.services..")
        .and()
        .haveSimpleNameNotEndingWith("Test")
        .should()
        .haveSimpleNameEndingWith("Service")
        .check(classes);
  }

  @Test
  void controllersShouldEndWithController() {

    classes()
        .that()
        .areAnnotatedWith(RestController.class)
        .should()
        .haveSimpleNameEndingWith("Controller")
        .check(classes);
  }

  @Test
  void rulesShouldEndWithRule() {

    classes()
        .that()
        .resideInAPackage("..domain.rules..")
        .and()
        .haveSimpleNameNotEndingWith("Test")
        .should()
        .haveSimpleNameEndingWith("Rule")
        .check(classes);
  }

  @Test
  void configsShouldEndWithConfig() {

    classes()
        .that()
        .resideInAPackage("..configs..")
        .should()
        .haveSimpleNameEndingWith("Config")
        .check(classes);
  }

  @Test
  void filtersShouldEndWithFilter() {

    classes()
        .that()
        .resideInAPackage("..filters..")
        .should()
        .haveSimpleNameEndingWith("Filter")
        .check(classes);
  }
}
