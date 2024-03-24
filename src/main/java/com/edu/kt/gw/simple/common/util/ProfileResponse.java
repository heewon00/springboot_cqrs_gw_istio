package com.edu.kt.gw.simple.common.util;

import java.util.Set;

record ProfileResponse(String username, Set<String> roles) {
}