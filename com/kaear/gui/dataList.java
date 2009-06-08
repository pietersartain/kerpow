package com.kaear.gui;

import com.kaear.common.*;
import com.kaear.cli.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


public interface dataList
{
	Vector makeList();
	String[] getColumnHeaders();
}
