package com.kaear.common;

public interface cliPlugin
{
	public void help();
	public void parseCommand(String[] command);
	public void parseCliCommand(String[] command);
}
