package com.challenge.token_validation_service.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RestController;

class PackageArchitectureTest {

  private static final String BASE_PACKAGE = "com.challenge.token_validation_service";

  private static JavaClasses classes;

  @BeforeAll
  static void setup() {

    classes = new ClassFileImporter().importPackages(BASE_PACKAGE);
  }

  @Test
  void controllersShouldResideInControllersPackage() {

    classes()
        .that()
        .areAnnotatedWith(RestController.class)
        .should()
        .resideInAPackage("..application.controllers..")
        .check(classes);
  }

  @Test
  void servicesShouldResideInServicesPackage() {

    classes()
        .that()
        .haveSimpleNameEndingWith("Service")
        .should()
        .resideInAPackage("..domain.services..")
        .check(classes);
  }

  @Test
  void rulesShouldResideInRulesPackage() {

    classes()
        .that()
        .haveSimpleNameEndingWith("Rule")
        .should()
        .resideInAPackage("..domain.rules..")
        .check(classes);
  }

  @Test
  void configsShouldResideInConfigsPackage() {

    classes()
        .that()
        .haveSimpleNameEndingWith("Config")
        .should()
        .resideInAPackage("..infrastructure.configs..")
        .check(classes);
  }

  @Test
  void filtersShouldResideInFiltersPackage() {

    classes()
        .that()
        .haveSimpleNameEndingWith("Filter")
        .should()
        .resideInAPackage("..infrastructure.filters..")
        .check(classes);
  }

  @Test
  void extractorsShouldResideInJwtPackage() {

    classes()
        .that()
        .haveSimpleNameEndingWith("Extractor")
        .should()
        .resideInAPackage("..infrastructure.jwt..")
        .check(classes);
  }
}
