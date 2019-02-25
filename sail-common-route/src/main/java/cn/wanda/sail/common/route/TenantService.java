package cn.wanda.sail.common.route;

import cn.wanda.sail.tenant.admin.facade.response.ResponseResult;
import cn.wanda.sail.tenant.admin.facade.response.dto.GroupDto;
import cn.wanda.sail.tenant.admin.facade.response.dto.TenantDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: chengyang.
 * @email: chengyang14@wanda.cn
 * @time: 17/4/25
 * @description: 描述
 */
@Service
public class TenantService {
    private Logger logger = LoggerFactory.getLogger(TenantService.class);

    @Autowired
    private TenantClient tenantClient;

    public List<TenantDto> queryTenantGroup(Long tenantId,String domain, String username, String membername) throws Exception{
    	
    	if(tenantId != null){
    		domain = null;
    		username = null;
    		membername = null;
    	}
//    	else if(domain != null){
//    		username = null;
//    		membername = null;
//    	}
    	domain = null;
    	logger.info("query tenant group tenantId:{}, userName:{}, domain:{}, memberName:{}", tenantId, username, domain, membername);
        ResponseResult<List<TenantDto>> result = tenantClient.queryTenantByUsernameOrMemberNameOrTenantId(username, membername, domain, tenantId);
        logger.info("调用结果：{}",JsonHelper.toJson(result));
        return result.getData();
    }

    public List<TenantDto> getTenanGroupListMock(Long tenantId,
                                             String domain, String userName, String membername) throws Exception{
        ResponseResult<List<TenantDto>> result = new ResponseResult<>();
        List<TenantDto> tenantDtos = new ArrayList<>();
        TenantDto tenantDto = new TenantDto();
        GroupDto groupDto = new GroupDto();
        groupDto.setEnglishName("wanda");
        tenantDto.setGroup(groupDto);
        tenantDtos.add(tenantDto);
        result.setData(tenantDtos);
        logger.info("调用结果：{}",JsonHelper.toJson(result));
        return result.getData();
    }
}
