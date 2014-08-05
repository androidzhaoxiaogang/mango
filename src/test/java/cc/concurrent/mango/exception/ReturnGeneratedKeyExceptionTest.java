/*
 * Copyright 2014 mango.concurrent.cc
 *
 * The Mango Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package cc.concurrent.mango.exception;

import cc.concurrent.mango.*;
import cc.concurrent.mango.support.Table;
import cc.concurrent.mango.support.Config;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author ash
 */
public class ReturnGeneratedKeyExceptionTest {

    private final static DataSource ds = Config.getDataSource();
    private final static Mango mango = new Mango(ds);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void before() throws Exception {
        Connection conn = ds.getConnection();
        Table.PERSON.load(conn);
        conn.close();
    }

    @Test
    public void test() {
        thrown.expect(ReturnGeneratedKeyException.class);
        thrown.expectMessage("please check whether the table has auto increment key");
        PersonDao dao = mango.create(PersonDao.class);
        dao.add(1, "name");
    }

    @DB
    interface PersonDao {
        @ReturnGeneratedId
        @SQL("insert into person(id, name) values(:1, :2)")
        public int add(int id, String name);
    }

}
