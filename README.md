# PriorityCommand [![Build Status](https://s.janmm14.de/prioritycommand-buildstatus)](https://s.janmm14.de/prioritycommand-ci)

A plugin for [Spigot](https://www.spigotmc.org/) minecraft server.

The prioritycommand command waits for the specified waiting ticks until the given command is executed. When another prioritycommand with the same prioritykey is executed within the waiting time and its priority is higher, that command will execute instead of the original scheduled command.

## Config
```yaml
waitingTicks: 2
```
| Option | Description |
| ------ | ----------- |
| waitingTicks | Time in ticks to wait until executing the highest priority command |

## Commands

| Command                | Alias | Usage                                                   | Description | 
| ---------------------- | ----- | ------------------------------------------------------- | ----------- |
| /prioritycommand       | /pc   | &lt;prioritykey&gt; &lt;priority&gt; &lt;command...&gt; | Add a command to given prioritykey with given priority |
| /prioritycommandcancel | /pcc  | &lt;prioritykey&gt;                                     | Cancel pending command execution for given priority key |

| term        | explanation |  
| ----------- | ----------- |
| prioritykey | arbitrary string to temporarily identify a priority group |
| priority    | number which defines the priority, higher number = higher priority |
