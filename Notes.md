# Notes

## Code Organization
* App.java - main entry point, uses Spring to load the CodeOrchestratorFactory
* CodeOrchestratorFactory - Wires up a LanguageOrchestrator of each type, and based on what the metadata says, generates using the appropriate type
    * JavaOrchestration
    * ObjectiveCOrchestration
    * RubyOrchestration
* LanguageOrchestrator - delegates off to a LanguageUtility to generate the code
* LanguageUtility - autowires in / delegates to the individual builders, this interface also defines the sub interfaces.  It needs to be refactored to better specialize the individual interfaces.
    * ModelBuilder
    * TestBuilder
    * ViewBuilder
    * ServiceBuilder
    * SingleViewControllerBuilder
    * ListViewControllerBuilder
    * ...

## Add a new Generator type
*
