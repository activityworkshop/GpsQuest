# GpsQuest

For more details, see the home page at https://activityworkshop.net/software/gpsquest/

Currently you can find here some data, including a couple of example `.quest` files and an `.xsd` for validation.
Under `source` you can find separate modules for the java code, currently focusing on the PC platform (see the roadmap for the various phases planned).
The project is in its very early stages so the code here only covers basic validation.

 * `quest_core` - basic code covering concepts like Zones, Triggers, Timers and Scenes. This will be used later by the simulator.
 * `quest_tool` - currently just does quest validation, may later be extended to provide other tools
 * `quest_test` - contains unit tests, won't be deployed at runtime
 * `quest_sim` - contains the basics of the simulator for the PC

Current status: the validator is in a usable state, giving warnings and errors appropriately.  The basics of the simulator window are now being built (Phase 2).
