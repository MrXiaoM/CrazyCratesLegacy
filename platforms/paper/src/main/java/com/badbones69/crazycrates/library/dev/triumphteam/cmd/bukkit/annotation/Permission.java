/**
 * MIT License
 *
 * Copyright (c) 2019-2021 Matt
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.badbones69.crazycrates.library.dev.triumphteam.cmd.bukkit.annotation;

import org.bukkit.permissions.PermissionDefault;

import java.lang.annotation.*;

/**
 * Annotate a method using this Annotation to add a required permission.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
public @interface Permission {

    /**
     * The permission.
     *
     * @return The permission's main node.
     */
    String[] value();

    /**
     * Represents the possible default values for permissions.
     *
     * @return The permission's {@link PermissionDefault}.
     * @see PermissionDefault
     */
    PermissionDefault def() default PermissionDefault.OP;

    /**
     * A brief description of the permission.
     *
     * @return The permission's description.
     * @see org.bukkit.permissions.Permission#getDescription()
     */
    String description() default "";
}
