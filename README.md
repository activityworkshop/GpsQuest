# GpsQuest

For more details, see the home page at https://activityworkshop.net/software/gpsquest/

Currently you can find here some data, including an example `.quest` file and an `.xsd` for validation.
Under `source` you can find separate modules for the java code, currently focusing on the PC platform (see the roadmap for the various phases planned).
The project is in its very early stages so the code here only covers basic validation.

 * `quest_core` - basic code covering concepts like Zones, Triggers, Timers and Scenes. This will be used later by the simulator.
 * `quest_tool` - currently just does quest validation, may later be extended to provide other tools
 * `quest_test` - contains unit tests, won't be deployed at runtime

Phase 1 (validation) is still underway, following which the PC-based simulator will be started.
