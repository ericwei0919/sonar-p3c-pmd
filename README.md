# SonarQube P3C PMD Plugin

Sonar-P3C-PMD is a plugin that provides coding rules from [P3C](https://github.com/alibaba/p3c).
Please note: This sonar plugin uses PMD version 6.13.0, and PMD/PMD Unit Tests rules are **not** included. **It means this plugin can work with or without PMD plugin separately.**

This repo add all P3C-PMD rules (version 2.0.0 has 56 rules):
```properties
##p3c##
#AlibabaJavaComments
rule.p3c-pmd.CommentsMustBeJavadocFormatRule.name=Javadoc should be used for classes, class variables and methods. The format should be '/** comment **/', rather than '// xxx'.
rule.p3c-pmd.AbstractMethodOrInterfaceMethodMustUseJavadocRule.name=Abstract methods (including methods in interface) should be commented by Javadoc.
rule.p3c-pmd.ClassMustHaveAuthorRule.name=Every class should include information of author(s) and date.
rule.p3c-pmd.EnumConstantsMustHaveCommentRule.name=All enumeration type fields should be commented as Javadoc style.
rule.p3c-pmd.AvoidCommentBehindStatementRule.name=Single line comments in a method should be put above the code to be commented, by using // and multiple lines by using /* */.
rule.p3c-pmd.RemoveCommentedCodeRule.name=Codes or configuration that is noticed to be obsoleted should be resolutely removed from projects.

#AlibabaJavaConcurrent
rule.p3c-pmd.ThreadPoolCreationRule.name=Manually create thread pool is better.
rule.p3c-pmd.AvoidUseTimerRule.name=Use ScheduledExecutorService instead.
rule.p3c-pmd.AvoidManuallyCreateThreadRule.name=Explicitly creating threads is not allowed, use thread pool instead.
rule.p3c-pmd.ThreadShouldSetNameRule.name=A meaningful thread name is helpful to trace the error information,so assign a name when creating threads or thread pools.
rule.p3c-pmd.AvoidCallStaticSimpleDateFormatRule.name=SimpleDataFormat is unsafe, do not define it as a static variable. If have to, lock or DateUtils class must be used.
rule.p3c-pmd.ThreadLocalShouldRemoveRule.name=type 'ThreadLocal' must call remove() method at least one times.
rule.p3c-pmd.AvoidConcurrentCompetitionRandomRule.name=Avoid using [Math.random()] by multiple threads.
rule.p3c-pmd.CountDownShouldInFinallyRule.name=should be called in finally block.

#AlibabaJavaConstants
rule.p3c-pmd.UpperEllRule.name='L' instead of 'l' should be used for long or Long variable.
rule.p3c-pmd.UndefineMagicConstantRule.name=Magic values, except for predefined, are forbidden in coding.

#AlibabaJavaExceptions
rule.p3c-pmd.MethodReturnWrapperTypeRule.name=If the return type is primitive, return a value of wrapper class may cause NullPointerException.
rule.p3c-pmd.AvoidReturnInFinallyRule.name=Never use return within a finally block.
rule.p3c-pmd.TransactionMustHaveRollbackRule.name=Attribute rollbackFor of annotation Transactional must be set.

#AlibabaJavaFlowControl
rule.p3c-pmd.SwitchStatementRule.name=In a switch block, each case should be finished by break/return. 
rule.p3c-pmd.NeedBraceRule.name=Braces are used with if, else, for, do and while statements, even if the body contains only a single statement. 
rule.p3c-pmd.AvoidComplexConditionRule.name=Do not use complicated statements in conditional statements.
rule.p3c-pmd.AvoidNegationOperatorRule.name=The negation operator is not easy to be quickly understood.

#AlibabaJavaNaming
rule.p3c-pmd.ClassNamingShouldBeCamelRule.name=Class names should be nouns in UpperCamelCase except domain models: DO, BO, DTO, VO, etc.
rule.p3c-pmd.AbstractClassShouldStartWithAbstractNamingRule.name=Abstract class names must start with Abstract or Base.
rule.p3c-pmd.ExceptionClassShouldEndWithExceptionRule.name=Must be ended with Exception.
rule.p3c-pmd.TestClassShouldEndWithTestNamingRule.name=Test cases should be ended with Test.
rule.p3c-pmd.LowerCamelCaseVariableNamingRule.name=Method names, parameter names, member variable names, and local variable names should be written in lowerCamelCase.
rule.p3c-pmd.AvoidStartWithDollarAndUnderLineNamingRule.name=All names should not start or end with an underline or a dollar sign.
rule.p3c-pmd.ServiceOrDaoClassShouldEndWithImplRule.name=Constant variable names should be written in upper characters separated by underscores.
rule.p3c-pmd.ConstantFieldShouldBeUpperCaseRule.name=Constant variable names should be written in upper characters separated by underscores.
rule.p3c-pmd.PackageNamingRule.name=All Service and DAO classes must be interface based on SOA principle. Implementation class names.
rule.p3c-pmd.BooleanPropertyShouldNotStartWithIsRule.name=Do not add 'is' as prefix while defining Boolean variable.
rule.p3c-pmd.ArrayNamingShouldHaveBracketRule.name=Brackets are a part of an Array type. The definition could be: String[] args

#AlibabaJavaOop
rule.p3c-pmd.EqualsAvoidNullRule.name=Equals should be invoked by a constant or an object that is definitely not null.
rule.p3c-pmd.WrapperTypeEqualityRule.name=The wrapper classes should be compared by equals method rather than by symbol of '==' directly.
rule.p3c-pmd.PojoMustUsePrimitiveFieldRule.name=Rules for using primitive data types and wrapper classes.
rule.p3c-pmd.PojoNoDefaultValueRule.name=While defining POJO classes like DO, DTO, VO, etc., do not assign any default values to the members.
rule.p3c-pmd.PojoMustOverrideToStringRule.name=We can call the toString method in a POJO directly to print property values.
rule.p3c-pmd.StringConcatRule.name=Use the append method in StringBuilder inside a loop body when concatenating multiple strings.
rule.p3c-pmd.BigDecimalAvoidDoubleConstructorRule.name=Avoid using the constructor BigDecimal(double) to convert double value to a BigDecimal object.

#AlibabaJavaOrm
rule.p3c-pmd.IbatisMethodQueryForListRule.name=iBatis built in queryForList(String statementName, int start, int size) is not recommended.

#AlibabaJavaOthers
rule.p3c-pmd.AvoidPatternCompileInMethodRule.name=When using regex, precompile needs to be done in order to increase the matching performance.
rule.p3c-pmd.AvoidApacheBeanUtilsCopyRule.name=Avoid using *Apache Beanutils* to copy attributes.
rule.p3c-pmd.AvoidNewDateGetTimeRule.name=Use System.currentTimeMillis() to get the current millisecond. Do not use new Date().getTime(). 
rule.p3c-pmd.AvoidMissUseOfMathRandomRule.name=The return type of Math.random() is double, value range is 0<=x<1 (0 is possible).
rule.p3c-pmd.MethodTooLongRule.name=The total number of lines for a method should not be more than 80.
rule.p3c-pmd.UseRightCaseForDateFormatRule.name=Date format string is error, when doing date formatting, 'y' should be written in lowercase for 'year'.
rule.p3c-pmd.AvoidDoubleOrFloatEqualCompareRule.name=To judge the equivalence of floating-point numbers, == cannot be used for primitive types, while equals cannot be used for wrapper classes.

#AlibabaJavaSets
rule.p3c-pmd.ClassCastExceptionWithToArrayRule.name=Do not use toArray method without arguments.
rule.p3c-pmd.UnsupportedExceptionWithModifyAsListRule.name=Do not use methods which will modify the list after using Arrays.asList to convert array to list.
rule.p3c-pmd.ClassCastExceptionWithSubListToArrayListRule.name=Do not cast subList in class ArrayList, otherwise ClassCastException will be thrown.
rule.p3c-pmd.ConcurrentExceptionWithModifyOriginSubListRule.name=When using subList, be careful to modify the size of original list. 
rule.p3c-pmd.DontModifyInForeachCircleRule.name=Do not remove or add elements to a collection in a foreach loop.
rule.p3c-pmd.CollectionInitShouldAssignCapacityRule.name=HashMap should set a size when initializing.

#AlibabaVelocityOthers
rule.p3c-pmd.UseQuietReferenceNotationRule.name=Variables must add exclamatory mark when passing to velocity engine from backend, ${var}--add '!' after '$'.
```

## Installation

1. Clone the repo, and run `mvn package` to build plugin package.
2. Put the output jar file, such as sonar-p3c-pmd-plugin-2.0.0.jar into ${sonarqube}/extensions/plugins
3. Restart sonarqube server.

## Usage

Usage should be straight forward:
1. Activate some P3C PMD rules in your quality profile.
2. Run an analysis.

## License

Sonar-P3C-PMD is licensed under the [GNU Lesser General Public License, Version 3.0](https://github.com/jensgerdes/sonar-pmd/blob/master/LICENSE.md).

Parts of the rule descriptions displayed in SonarQube have been extracted from [PMD](https://pmd.github.io/) and are licensed under a [BSD-style license](https://github.com/pmd/pmd/blob/master/LICENSE).  

